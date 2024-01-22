package pl.rentowne.rentedObject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.repository.custom.RentedObjectRepositoryCustom;

import java.util.List;

/**
 * Repozytorium wynajętych obiektów
 */
public interface RentedObjectRepository extends JpaRepository<RentedObject, Long>, RentedObjectRepositoryCustom {

    @Query("SELECT ro FROM RentedObject ro LEFT JOIN FETCH ro.meters WHERE ro.apartment.id = :apartmentId")
    List<RentedObject> findByApartmentIdWithMeters(@Param("apartmentId") Long apartmentId);
}
