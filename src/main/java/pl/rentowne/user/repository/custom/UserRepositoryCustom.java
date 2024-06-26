package pl.rentowne.user.repository.custom;

import com.querydsl.core.Tuple;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.UserBasicDto;

import java.util.Optional;

/**
 * Rozszerzone repozytorium użytkowników
 */
public interface UserRepositoryCustom {

    /**
     * Zwraca encę użytkownika po jego adresie email
     *
     * @param email adres email
     * @return encja użytkownika {@link User}
     */
    Optional<User> findByEmail(String email);

    /**
     * Sprawdza czy istnieje użytkownik z podanym mailem
     * @param email - email użytkownika
     * @return true - istnieje / false - nie istnieje
     */
    boolean isUserExistsByEmail(String email);

    /**
     * Pobiera id użytkownika
     * @param email email użytkownika
     * @return {@link UserBasicDto}
     */
    UserBasicDto getByEmail(String email);

    /**
     * Pobiera użytkownika po hashu z linka resetu hasła
     * @param hash hash resetu hasła
     * @return dane użytkownika
     */
    Optional<User> findByHash(String hash);

    /**
     * Pobiera pełne dane użytkownika po adresie email
     * @param loggedUserEmail adres email
     * @return dane użytkownika
     */
    User getFullUserDataByEmail(String loggedUserEmail);


    /**
     * Pobiera zdjęcie oraz preferowany język użytkownika
     * @param loggedUserEmail email użytkownika
     * @return zdjecie oraz preferowany język
     */
    Tuple getUserProfileImageAndLang(String loggedUserEmail);

}
