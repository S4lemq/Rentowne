package pl.rentowne.user.repository.custom;

import pl.rentowne.user.model.User;

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
}
