package pl.rentowne.user.service.impl;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.address.model.dto.AddressDto;
import pl.rentowne.common.mail.EmailClientService;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.payment_card.model.dto.PaymentCardDto;
import pl.rentowne.payment_card.service.PaymentCardService;
import pl.rentowne.tenant.repository.TenantRepository;
import pl.rentowne.user.model.PreferredLanguage;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.UserBasicDto;
import pl.rentowne.user.model.dto.UserDto;
import pl.rentowne.user.repository.UserRepository;
import pl.rentowne.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import static pl.rentowne.user.model.QUser.user;
import static pl.rentowne.util.StringUtils.isNotNullOrEmpty;

@Service("USER_SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailClientService emailService;
    private final TenantRepository tenantRepository;
    private final PaymentCardService paymentCardService;

    @Override
    public User getByEmail(String email) throws RentowneNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new RentowneNotFoundException(email));
    }

    @Override
    @Transactional(readOnly = true)
    public UserBasicDto getLoggedUser() throws RentowneBusinessException {
        return userRepository.getByEmail(getLoggedUserEmail());
    }

    @Override
    @Transactional(readOnly = true)
    public Long getRentedObjectByLoggedTenant() throws RentowneBusinessException {
        String email = getLoggedUserEmail();
        return tenantRepository.getRentedObjectIdByTenantEmail(email);
    }

    @Override
    public Optional<User> findByHash(String hash) {
        return userRepository.findByHash(hash);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getFullUserData() {
        User user = userRepository.getFullUserDataByEmail(getLoggedUserEmail());
        return UserDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .image(user.getImage())
                .phoneNumber(user.getPhoneNumber())
                .preferredLanguage(user.getPreferredLanguage())
                .paymentCardDto(user.getPaymentCard() != null ? PaymentCardDto.asDto(user.getPaymentCard()) : null)
                .addressDto(user.getAddress() != null ? AddressDto.asDto(user.getAddress()) : null)
                .build();
    }

    @Override
    @Transactional
    public void updateUser(UserDto userDto) {
        User user = userRepository.findById(userDto.getId()).orElseThrow(() -> new RentowneNotFoundException(userDto.getEmail()));
        if (validateAddress(userDto.getAddressDto())) {
            user.setAddress(AddressDto.asEntity(userDto.getAddressDto()));
        }
        if (validatePaymentCard(userDto.getPaymentCardDto())) {
            paymentCardService.save(userDto.getPaymentCardDto(), user);
        }

        boolean passwordToChange = userDto.getPassword() != null && userDto.getPassword().equals(userDto.getRepeatPassword());
        if (passwordToChange) {
            if (userDto.getOldPassword() == null) {
                throw new RentowneBusinessException(RentowneErrorCode.BAD_OLD_PASSWORD);
            }
            if (!passwordEncoder.matches(userDto.getOldPassword(), user.getPassword())) {
                throw new RentowneBusinessException(RentowneErrorCode.BAD_OLD_PASSWORD);
            }
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setImage(userDto.getImage());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPreferredLanguage(userDto.getPreferredLanguage());

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
    public UserDto getUserProfileImageAndLang() {
        Tuple userProfileData = userRepository.getUserProfileImageAndLang(getLoggedUserEmail());

        String image = userProfileData.get(user.image);
        PreferredLanguage preferredLanguage = userProfileData.get(user.preferredLanguage);

        return UserDto.builder()
                .image(image)
                .preferredLanguage(preferredLanguage)
                .build();
    }

    /**
     * Waluduje adres, albo wszystkie pola wymagane puste (nr budynku, kod pocztowy, wojewódtwo, miejscowość) albo wypełnione
     * @param address dane adresowe
     * @return true/false
     */
    private boolean validateAddress(AddressDto address) {
        boolean isAnyFieldSet = isNotNullOrEmpty(address.getBuildingNumber())
                || isNotNullOrEmpty(address.getZipCode())
                || isNotNullOrEmpty(address.getCityName())
                || isNotNullOrEmpty(address.getVoivodeship());

        if (isAnyFieldSet) {
            boolean areAllFieldsSet = isNotNullOrEmpty(address.getBuildingNumber())
                    && isNotNullOrEmpty(address.getZipCode())
                    && isNotNullOrEmpty(address.getCityName())
                    && isNotNullOrEmpty(address.getVoivodeship());

            if (!areAllFieldsSet) {
                throw new RentowneBusinessException(RentowneErrorCode.EMPTY_REQUIRED_ADDRESS_FIELDS);
            }
            return true; // Wszystkie wymagane pola są ustawione
        }
        return false; // Żadne z pól nie jest ustawione, więc adres nie zostanie zaktualizowany
    }

    /**
     * Waliduje dane karty płatniczej, albo wszyskie pola wypełnione, albo żadne, nie może być tylko część wypełniona
     * @param cardDto dane karty płatniczej
     * @return true/false
     */
    private boolean validatePaymentCard(PaymentCardDto cardDto) {
        if (cardDto == null) {
            return false; // Obiekt PaymentCardDto jest null, więc nie ma co walidować
        }

        // Sprawdź, czy jakiekolwiek pole nie jest null i nie jest puste (dla String) lub większe od 0 (dla int)
        boolean isAnyFieldSet = isNotNullOrEmpty(cardDto.getNumber()) || isNotNullOrEmpty(cardDto.getName())
                || cardDto.getMonth() > 0 || cardDto.getYear() > 0 || cardDto.getCvv() > 0;

        if (isAnyFieldSet) {
            // Jeżeli jakiekolwiek pole jest ustawione, sprawdź czy wszystkie są ustawione
            boolean areAllFieldsSet = isNotNullOrEmpty(cardDto.getNumber()) && isNotNullOrEmpty(cardDto.getName())
                    && cardDto.getMonth() > 0 && cardDto.getYear() > 0 && cardDto.getCvv() > 0;

            if (!areAllFieldsSet) {
                throw new RentowneBusinessException(RentowneErrorCode.EMPTY_REQUIRED_PAYMENT_CARD_FIELDS);
            }
            return true; // Wszystkie pola są poprawnie wypełnione
        }
        return false; // Żadne z pól nie jest ustawione, więc karta nie zostanie zapisana
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
