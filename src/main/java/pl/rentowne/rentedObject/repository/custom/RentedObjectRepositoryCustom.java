package pl.rentowne.rentedObject.repository.custom;

import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;

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

    /**
     * Usuwa wszystkie wynajęte obiekty po id mieszkania
     * @param apartmentId id mieszkania
     */
    void deleteAllByApartmentId(Long apartmentId);

    /**
     * Pobiera wszystkie obiekty do wynajęcia danego użytkownika
     * @param userId id użytkownika
     * @return lista obiektów do wynajęcia
     */
    List<RentedObjectDto> getAllRentedObjectDtos(Long userId);
}
