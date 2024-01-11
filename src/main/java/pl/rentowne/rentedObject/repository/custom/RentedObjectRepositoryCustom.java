package pl.rentowne.rentedObject.repository.custom;

import pl.rentowne.rentedObject.model.RentedObject;

import java.util.List;

/**
 * Rozszerzone repozytorium wynajętych obiektów
 */
public interface RentedObjectRepositoryCustom {

    /**
     * Pobiera wszystkie obiekty do wynajęcia po id mieszkania
     * @param apartmentId id mieszkania
     * @return lista encji obiektów do wynajęcia
     */
    List<RentedObject> getAllByApartmentId(Long apartmentId);
}
