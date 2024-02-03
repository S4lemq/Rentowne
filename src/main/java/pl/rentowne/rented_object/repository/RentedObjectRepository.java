package pl.rentowne.rented_object.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.rentowne.rented_object.model.RentedObject;
import pl.rentowne.rented_object.repository.custom.RentedObjectRepositoryCustom;

import java.util.List;

/**
 * Repozytorium wynajętych obiektów
 */
public interface RentedObjectRepository extends JpaRepository<RentedObject, Long>, RentedObjectRepositoryCustom {

    @Query("SELECT ro FROM RentedObject ro LEFT JOIN FETCH ro.meters WHERE ro.apartment.id = :apartmentId")
    List<RentedObject> findByApartmentIdWithMeters(@Param("apartmentId") Long apartmentId);
}
