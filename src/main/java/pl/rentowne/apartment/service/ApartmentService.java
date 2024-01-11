package pl.rentowne.apartment.service;

import pl.rentowne.apartment.model.dto.AddApartmentDto;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;

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
    Long addApartment(AddApartmentDto apartmentDto) throws RentowneBusinessException;

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
    void updateApartment(AddApartmentDto apartmentDto, Long apartmentId) throws RentowneBusinessException;

    /**
     * Usuwa mieszkanie po id
     * @param id id mieszkania
     * @return
     */
    void deleteApartment(Long id);
}
