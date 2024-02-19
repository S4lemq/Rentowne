package pl.rentowne.apartment.service;

import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.model.dto.ApartmentHousingProviderRequest;
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

    /**
     * Przypisuje dostawcę do mieszkania
     * @param dto id mieszkania i list id dostawców
     * @throws RentowneNotFoundException nie znaleziono dostawcy/mieszkania
     */

    /**
     * Przypisuje mieszkanie do dostawcy świadczeń
     * @param dto {@link ApartmentHousingProviderRequest} dto mieszkania i idków dostawców
     * @throws RentowneNotFoundException nie znaleziono mieszkania/dostawcy
     */
    void addHousingProviders(ApartmentHousingProviderRequest dto) throws RentowneNotFoundException;

    /**
     * zlicza ilość obiektów w mieszkaniu po id obiektu
     * @param rentedObjectId id obiektu
     * @return ilość obiektów w mieszkaniu
     */
    int getCountOfRentedObjectsInApartment(long rentedObjectId);

    /**
     * Służy do przypinania nieruchomości
     * @param isPinned czy przypięto
     * @param id id mieszkania
     */
    void pinApartment(boolean isPinned, long id);
}
