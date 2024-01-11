package pl.rentowne.rentedObject.service;

import pl.rentowne.rentedObject.model.RentedObject;

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
}
