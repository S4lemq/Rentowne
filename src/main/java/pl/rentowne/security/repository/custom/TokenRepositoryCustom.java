package pl.rentowne.security.repository.custom;

import pl.rentowne.security.model.Token;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium tokenów
 */
public interface TokenRepositoryCustom {

    /**
     * Zwraca wszystkie działające tokeny danego użytkownika
     *
     * @param userId id użytkownika
     * @return lista tokenów {@link Token}
     */
    List<Token> findAllValidTokensByUser(Long userId);

    /**
     * Zwraca token
     *
     * @param token wartość tokena
     * @return token {@link Token}
     */
    Optional<Token> findByToken(String token);

}
