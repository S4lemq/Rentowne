package pl.rentowne.user.service;

import pl.rentowne.user.model.User;

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
}
