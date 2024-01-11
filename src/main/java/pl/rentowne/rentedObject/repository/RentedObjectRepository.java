package pl.rentowne.rentedObject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.repository.custom.RentedObjectRepositoryCustom;

/**
 * Repozytorium wynajętych obiektów
 */
public interface RentedObjectRepository extends JpaRepository<RentedObject, Long>, RentedObjectRepositoryCustom {
}
