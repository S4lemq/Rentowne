package pl.rentowne.user.repository.custom;

import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.UserBasicDto;
import pl.rentowne.user.model.dto.UserDto;

import java.util.Optional;

/**
 * Repozytorium użytkowników
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
     * Pobiera dane użytkownika po adresie email
     * @param email adres email
     * @return dane użytkownika
     */
    UserDto getUserDtoByEmail(String email);

    /**
     * Pobiera nazwę pliku graficznego profilu użytkownika
     * @param loggedUserEmail email zalogowanego użytkownika
     * @return nazwa pliku
     */
    String getUserProfileImage(String loggedUserEmail);
}
