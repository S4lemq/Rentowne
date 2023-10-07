package pl.rentowne.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.user.model.User;
import pl.rentowne.user.repository.custom.UserRepositoryCustom;

/**
 * Repozytorium użytkowników
 */
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
