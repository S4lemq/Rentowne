package pl.rentowne.dataTable.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.dataTable.repository.custom.DTRepositoryCustom;
import pl.rentowne.user.model.User;

import java.util.UUID;

/**
 * Repozytorium dla tabeli z danymi
 */
public interface DTRepository extends JpaRepository<User, UUID>, DTRepositoryCustom {

}
