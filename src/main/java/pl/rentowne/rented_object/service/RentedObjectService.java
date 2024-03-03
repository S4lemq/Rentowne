package pl.rentowne.rented_object.service;

import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.rented_object.model.RentedObject;
import pl.rentowne.rented_object.model.dto.RentedObjectDto;

import java.util.List;
import java.util.Set;

public interface RentedObjectService {

    /**
     * Zapisuje wszystkie obiekty do wynajęcia
     * @param rentedObjects obiekty do zapisu
     */
    void saveAll(Set<RentedObject> rentedObjects);

    /**
     * Pobiera obiekty do wynajęcia po id mieszkania
     * @param apartmentId id mieszkania
     * @return lista obiektów należących do danego mieszkania
     */
    List<RentedObject> getAllByApartmentId(Long apartmentId);

    /**
     * Usuwa wszystkie podane obiekty
     * @param toDelete obiekty do usunięcia
     */
    void deleteAll(List<RentedObject> toDelete);

    /**
     * Usuwa wszystkie wynajęte obiekty po id mieszkania
     * @param apartmentId id mieszkania
     */
    void deleteAllByApartmentId(Long apartmentId);

    /**
     * Pobiera wszystkie dto'sy obiektów do wynajęcia wraz z licznikami po id mieszkania
     * @param apartmentId id mieszkania
     * @return {@link List< RentedObjectDto }
    */
    List<RentedObjectDto> getAllRentedObjectDtosWithMeters(Long apartmentId);

    /**
     * Pobiera wszystkie obiekty do wynajęcia zalogowanego użytkownika
     * @return lista obiektów do wynajęcia zalogowanego użytkownika
     * @throws RentowneBusinessException wyjątek biznesowy
     */
    List<RentedObjectDto> getAllRentedObjectDtos() throws RentowneBusinessException;

    /**
     * Pobiera wszystkie obiekty do wynajęcia po id mieszkania i opcjonalnie pobiera jeszcze dany obiekt po drugim parametrze
     * @param apartmentId id mieszkania
     * @param rentedObjectId id obiektu opcjonalnego
     * @return lista obiektów do wynajęcia
     */
    List<RentedObjectDto> getAllByApartmentAndOptionalRentedObject(Long apartmentId, Long rentedObjectId) throws RentowneBusinessException;

}
