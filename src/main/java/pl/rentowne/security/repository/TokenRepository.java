package pl.rentowne.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.security.model.Token;
import pl.rentowne.security.repository.custom.TokenRepositoryCustom;

/**
 * Repozytorium tokenów
 */
public interface TokenRepository extends JpaRepository<Token, Long>, TokenRepositoryCustom {
}
