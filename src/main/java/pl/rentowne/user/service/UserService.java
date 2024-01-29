package pl.rentowne.user.service;

import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.UserBasicDto;

import java.util.Optional;

/**
 * Serwis użytkowników
 */
public interface UserService {

    /**
     * Zwraca użytkownika po adresie email
     *
     * @param email adres email
     * @return encja użytkownika
     */
    User getByEmail(String email) throws RentowneNotFoundException;

    /**
     * Zwraca zalogowanego aktualnie użytkowanika
     * @return UserBasicDto
     */
    UserBasicDto getLoggedUser() throws RentowneBusinessException;

    /**
     * Pobiera użytkownika po hashu z linka resetu hasła
     * @param hash hash resetu hasła
     * @return dane użytkownika
     */
    Optional<User> findByHash(String hash);
}
