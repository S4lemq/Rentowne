package pl.rentowne.apartment.repository.custom;

import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.exception.RentowneNotFoundException;

import java.util.List;
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

    /**
     * Pobiera id mieszkania po id obiektu do wynajęcia
     * @param rentedObjectId id obiektu do wynajęcia
     * @return id mieszkania
     */
    Long getApartmentIdByRentedObject(Long rentedObjectId);

    /**
     * Pobiera obiekty do wynajęcia danego mieszkania po fladze wynajęcia obiektów
     * @param apartmentId id mieszkania
     * @param isRented flaga wynajęcia obiektu
     * @return lista id obiektów do wynajęcia
     */
    List<Long> getAllRentedObjectsByApartmentIdAndRentedFlag(Long apartmentId, boolean isRented);

    /**
     * Aktualizuje flagę czy wynajęte
     * @param apartmentId id mieszkania
     * @param flag flaga
     */
    void updateRentedFlag(Long apartmentId, boolean flag);

    /**
     * Pobiera wszystkie mieszkania zalogowanego użytkownika po oraz opcjonalnie dodaje do wyników jeszcze przekazany parametr apartmentId
     * @param loggedUser zalogowany użytkownik
     * @param apartmentId id mieszkania
     * @return lista mieszkań
     */
    List<ApartmentDto> getAllApartmentsByLoggedUserAndApartment(Long loggedUser, Long apartmentId);

    List<ApartmentDto> getAllApartments(Long id);
}
