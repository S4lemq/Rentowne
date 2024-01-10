package pl.rentowne.apartment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.repository.custom.ApartmentRepositoryCustom;

/**
 * Repozytorium mieszkań
 */
public interface ApartmentRepository extends JpaRepository<Apartment, Long>, ApartmentRepositoryCustom {

    @Query("SELECT apartment.address.id FROM Apartment apartment WHERE apartment.id = :id")
    Long getAddressId(Long id);
}
