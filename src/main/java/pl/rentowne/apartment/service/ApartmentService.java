package pl.rentowne.apartment.service;

import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;

import java.util.List;

/**
 * Serwis mieszkań
 */
public interface ApartmentService {

    /**
     * Pobiera mieszkanie po id
     *
     * @param id id mieszkania
     * @return {@link ApartmentDto} dto mieszkania
     * @throws RentowneNotFoundException - nie znaleziono mieszkania
     */
    ApartmentDto getApartment(Long id) throws RentowneNotFoundException;

    /**
     * Dodaje mieszkanie
     * @param apartmentDto dto mieszkania
     * @return id zapisanego mieszkania
     */
    Long addApartment(ApartmentDto apartmentDto) throws RentowneBusinessException;

    /**
     * pobiera id adresu mieszkania
     * @param id id mieszkania
     * @return id adresu mieszkania
     */
    Long getAddressId(Long id);

    /**
     * aktualizuje dane mieszkania
     * @param apartmentDto dto mieszkania
     * @param apartmentId id mieszkania
     * @throws RentowneBusinessException wyjątek biznesowy
     */
    void updateApartment(ApartmentDto apartmentDto, Long apartmentId) throws RentowneBusinessException;

    /**
     * Usuwa mieszkanie po id
     * @param id id mieszkania
     * @return
     */
    void deleteApartment(Long id);

    /**
     * Pobiera wszystkie mieszkania zalogowanego użytkownika po oraz opcjonalnie dodaje do wyników jeszcze przekazany parametr apartmentId
     * @param apartmentId id mieszkania
     * @return lista mieszkań
     */
    List<ApartmentDto> getAllApartmentsByLoggedUserAndApartment(Long apartmentId) throws RentowneBusinessException;

    /**
     * Aktualizuje flagę czy wynajęte
     * @param rentedObjectId id obiektu
     */
    void updateRentedFlagOnApartment(Long rentedObjectId);

    List<ApartmentDto> getAllApartments() throws RentowneBusinessException;
}
