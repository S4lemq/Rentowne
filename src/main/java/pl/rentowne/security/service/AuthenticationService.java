package pl.rentowne.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.common.mail.EmailClientService;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.security.model.Token;
import pl.rentowne.security.model.TokenType;
import pl.rentowne.security.model.dto.AuthenticationRequest;
import pl.rentowne.security.model.dto.AuthenticationResponse;
import pl.rentowne.security.model.dto.RegisterRequest;
import pl.rentowne.security.repository.TokenRepository;
import pl.rentowne.tenant.model.dto.TenantDto;
import pl.rentowne.tfa.TwoFactorAuthenticationService;
import pl.rentowne.user.model.Role;
import pl.rentowne.user.model.User;
import pl.rentowne.user.repository.UserRepository;
import pl.rentowne.util.PasswordGenerator;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TwoFactorAuthenticationService tfaService;
    private final EmailClientService emailClientService;
    @Value("${application.serviceAddress}")
    private String serviceAddress;

    @Transactional
    public void createTenantAccount(TenantDto tenantDto) {
        User user = User.builder()
                .firstname(tenantDto.getFirstname())
                .lastname(tenantDto.getLastname())
                .email(tenantDto.getEmail())
                .password(passwordEncoder.encode(PasswordGenerator.generatePassword()))
                .role(Role.USER)
                .mfaEnabled(false)
                .build();

        User savedUser = userRepository.save(user);
        this.sendCreatedAccount(savedUser);
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) throws RentowneBusinessException {
        boolean isUserExistsByEmail = userRepository.isUserExistsByEmail(request.getEmail());
        if (isUserExistsByEmail) {
            throw new RentowneBusinessException(RentowneErrorCode.USER_ALREADY_EXISTS, "email");
        }

        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ADMIN)
                .mfaEnabled(request.isMfaEnabled())
                .build();

        if (request.isMfaEnabled()) {
            user.setSecret(tfaService.generateNewSecret());
        }

        user.setInsertOperator(user.getEmail());
        User savedUser = userRepository.save(user);

        if (!request.isMfaEnabled()) {
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .mfaEnabled(false)
                    .landlordAccess(true)
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .secretImageUri(tfaService.generateQrCodeImageUri(user.getSecret(), user.getEmail()))
                    .mfaEnabled(user.isMfaEnabled())
                    .build();
        }
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws RentowneBusinessException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RentowneBusinessException(RentowneErrorCode.BAD_CREDENTIALS);
        }
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(user.isMfaEnabled())
                .landlordAccess(user.getRole().equals(Role.ADMIN))
                .build();
    }

    @Transactional
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            User user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                String accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                AuthenticationResponse authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .mfaEnabled(false)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Transactional
    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) throws RentowneBusinessException {
        User user = userRepository
                .findByEmail(verificationRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("No user found with %S", verificationRequest.getEmail())
                ));
        if (tfaService.isOtpNotValid(user.getSecret(), verificationRequest.getCode())) {
            throw new RentowneBusinessException(RentowneErrorCode.WRONG_GOOGLE_AUTH_CODE);
        }
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(true)
                .landlordAccess(true)
                .build();
    }

    private void sendCreatedAccount(User user) {
        String hash = generateHashForLostPassword(user);
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now());
        emailClientService.getInstance()
                .send(user.getEmail(), "Witaj w aplikacji Rentowne", createWelcomeMessage(createLink(hash)));
    }

    private String createWelcomeMessage(String setPasswordLink) {
        return "Witaj w Rentowne!" +
                "\n\nTwoje konto w aplikacji Rentowne.pl zostało pomyślnie utworzone." +
                "\nUżywając adresu email podanego w trakcie podpisywania umowy najmu, możesz teraz ustanowić swoje hasło." +
                "\n\nProsimy, kliknij w poniższy link, aby ustawić hasło do aplikacji:" +
                "\n" + setPasswordLink +
                "\n\nDziękujemy za zaufanie i życzymy miłego użytkowania aplikacji." +
                "\nZespół Rentowne";
    }

    private String createLink(String hash) {
        return serviceAddress + "/tenant-password/" + hash;
    }

    private String generateHashForLostPassword(User user) {
        String toHash = user.getId() + user.getUsername() + user.getPassword() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .tokenValue(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        token.setInsertDate(LocalDateTime.now());
        token.setInsertOperator(user.getEmail());
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
