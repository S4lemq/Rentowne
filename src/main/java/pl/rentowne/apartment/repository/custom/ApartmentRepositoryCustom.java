package pl.rentowne.apartment.repository.custom;

import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.exception.RentowneNotFoundException;

import java.util.Optional;

/**
 * Rozszerzone repozytorium mieszkań
 */
public interface ApartmentRepositoryCustom {

    /**
     * Pobiera mieszkanie po id
     *
     * @param id id mieszkania
     * @return {@link ApartmentDto} dto mieszkania
     */
    Optional<Apartment> findApartmentById(Long id) throws RentowneNotFoundException;

}
