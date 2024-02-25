package pl.rentowne.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.common.mail.EmailClientService;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.security.model.dto.ChangePassword;
import pl.rentowne.security.model.dto.LostPasswordRequest;
import pl.rentowne.user.model.User;
import pl.rentowne.user.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class LostPasswordService {

    private final UserService userService;
    private final EmailClientService emailClientService;
    @Value("${application.serviceAddress}")
    private String serviceAddress;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void sendLostPasswordLink(LostPasswordRequest lostPasswordRequest) {
        User user;
        try {
            user = userService.getByEmail(lostPasswordRequest.getEmail());
        } catch (RentowneNotFoundException e) {
            log.info("User with e-mail {} tried to recover the password, but the account with this e-mail does not exist", lostPasswordRequest.getEmail());
            return;
        }
        String hash = generateHashForLostPassword(user);
        user.setHash(hash);
        user.setHashDate(LocalDateTime.now());
        log.info("User with email: {} tried recover the password:", lostPasswordRequest.getEmail());
        emailClientService.getInstance()
                .send(lostPasswordRequest.getEmail(), "Zresetuj hasło", createMessage(createLink(hash, lostPasswordRequest.getIsTenant())));
    }

    private String createMessage(String hashLink) {
        return new StringBuilder()
                .append("Szanowny Użytkowniku,")
                .append("\n\nOtrzymaliśmy prośbę o zresetowanie hasła do Twojego konta.")
                .append("\nAby ustawić nowe hasło, kliknij poniższy link:")
                .append("\n").append(hashLink)
                .append("\n\nLink do zmiany hasła będzie aktywny przez 1 godzinę. Jeśli nie żądałeś zmiany hasła, zignoruj tę wiadomość.")
                .append("\n\nZ poważaniem,")
                .append("\nZespół Rentowne")
                .toString();
    }


    private String createLink(String hash, boolean isTenant) {
        if (isTenant) {
            return serviceAddress + "/tenant-lost-password/" + hash;
        } else {
            return serviceAddress + "/lost-password/" + hash;
        }
    }

    private String generateHashForLostPassword(User user) {
        String toHash = user.getId() + user.getUsername() + user.getPassword() + LocalDateTime.now();
        return DigestUtils.sha256Hex(toHash);
    }

    @Transactional
    public void changePassword(ChangePassword changePassword) throws RentowneBusinessException {
        User user = userService.findByHash(changePassword.getHash())
                .orElseThrow(() -> new RentowneBusinessException(RentowneErrorCode.BAD_LINK));

        if(user.getHashDate().plusMinutes(60).isAfter(LocalDateTime.now())){
            user.setPassword(passwordEncoder.encode(changePassword.getPassword()));
            user.setHash(null);
            user.setHashDate(null);
        } else {
            throw new RentowneBusinessException(RentowneErrorCode.LINK_EXPIRED);
        }
    }

}
