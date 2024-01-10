package pl.rentowne.apartment.service;

import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.dto.ApartmentDto;
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
     * @param apartment mieszkanie
     * @return id zapisanego mieszkania
     */
    Long addApartment(Apartment apartment);

    /**
     * aktualizuje dane mieszkania
     * @param apartment zaktualizowane dane mieszkania
     */
    void updateApartment(Apartment apartment);

    /**
     * pobiera id adresu mieszkania
     * @param id id mieszkania
     * @return id adresu mieszkania
     */
    Long getAddressId(Long id);
}
