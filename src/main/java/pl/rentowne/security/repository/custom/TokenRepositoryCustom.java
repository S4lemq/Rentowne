package pl.rentowne.security.repository.custom;

import pl.rentowne.security.model.Token;

import java.util.List;

/**
 * Repozytorium tokenów
 */
public interface TokenRepositoryCustom {

    List<Token> findAllValidTokensByUser(Long userId);
}
