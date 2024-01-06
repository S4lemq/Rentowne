package pl.rentowne.user.service;

import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.UserBasicDto;

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
    User getByEmail(String email);

    /**
     * Zwraca zalogowanego aktualnie użytkowanika
     * @return UserBasicDto
     */
    UserBasicDto getLoggedUser() throws RentowneBusinessException;
}
