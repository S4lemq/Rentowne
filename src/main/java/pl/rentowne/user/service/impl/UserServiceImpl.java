package pl.rentowne.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.common.mail.EmailClientService;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.UserBasicDto;
import pl.rentowne.user.model.dto.UserDto;
import pl.rentowne.user.repository.UserRepository;
import pl.rentowne.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

@Service("USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailClientService emailService;

    @Override
    public User getByEmail(String email) throws RentowneNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new RentowneNotFoundException(email));
    }

    @Override
    public UserBasicDto getLoggedUser() throws RentowneBusinessException {
        return userRepository.getByEmail(getLoggedUserEmail());
    }

    @Override
    public Optional<User> findByHash(String hash) {
        return userRepository.findByHash(hash);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser() {
        return userRepository.getUserDtoByEmail(getLoggedUserEmail());
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto) {

        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new RentowneNotFoundException(userDto.getEmail()));

        boolean passwordToChange = userDto.getPassword() != null && userDto.getPassword().equals(userDto.getRepeatPassword());
        if (passwordToChange) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setImage(userDto.getImage());

        userRepository.save(user);

        if (passwordToChange) {
            emailService.getInstance().send(
                    userDto.getEmail(),
                    "Aktualizacja hasła i danych osobowych w aplikacji Rentowne",
                    this.getChangePasswordAndPersonalDataMsg());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String getUserProfileImage() {
        return userRepository.getUserProfileImage(getLoggedUserEmail());
    }

    private String getLoggedUserEmail() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        if (loggedInUser == null || !(loggedInUser.getPrincipal() instanceof UserDetails)) {
            throw new RentowneBusinessException(RentowneErrorCode.USER_IS_NOT_LOGGED);
        }

        UserDetails userDetails = (UserDetails) loggedInUser.getPrincipal();
        String email = userDetails.getUsername();
        return email;
    }

    private String getChangePasswordAndPersonalDataMsg() {
        LocalDateTime now = LocalDateTime.now();
        String formattedDate = now.format(DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm", new Locale("pl", "PL")));

        return "Szanowny Użytkowniku,\n\n"
                + "Chcielibyśmy Cię poinformować, że w dniu " + formattedDate + " dokonano zmian w Twoich danych osobowych oraz haśle w aplikacji Rentowne.\n\n"
                + "Bezpieczeństwo Twoich danych jest dla nas priorytetem. Jeśli to Ty dokonałeś tej zmiany, nie ma potrzeby podejmowania żadnych działań. Jednakże, jeśli uważasz, że zmiana danych osobowych i hasła nastąpiła bez Twojej wiedzy lub zgody, prosimy o natychmiastowy kontakt z naszym działem wsparcia technicznego na adres email: wsparcie@rentowne.com.\n\n"
                + "Zalecamy regularne aktualizowanie hasła oraz korzystanie z silnych, unikalnych haseł, aby zapewnić dodatkową warstwę ochrony Twojego konta.\n\n"
                + "Dziękujemy za korzystanie z aplikacji Rentowne i cieszymy się, że jesteś z nami.\n\n"
                + "Z poważaniem,\n"
                + "Zespół Rentowne";
    }

}
