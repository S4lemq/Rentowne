package pl.rentowne.rented_object.repository.custom;

import pl.rentowne.rented_object.model.RentedObject;
import pl.rentowne.rented_object.model.dto.RentedObjectDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    /**
     * Aktualizuje flagę czy wynajęte
     * @param rentedObjectId id obiektu do wynajęcia
     * @param flag flaga
     */
    void updateRentedFlag(Long rentedObjectId, boolean flag);

    /**
     * Pobiera wszystkie obiekty do wynajęcia po id mieszkania i opcjonalnie pobiera jeszcze dany obiekt po drugim parametrze
     * @param apartmentId id mieszkania
     * @param rentedObjectId id obiektu opcjonalnego
     * @return lista obiektów do wynajęcia
     */
    List<RentedObjectDto> getAllByApartmentAndOptionalRentedObject(Long apartmentId, Long rentedObjectId);

    /**
     * Aktualizuje w obiekcie ostatnią datę rozliczenia oraz kwotę całkowitą do zapłaty przez najemcę
     *
     * @param rentedObjectId id obiektu do wynajęcia
     * @param totalSum       kwota całkowita do zapłaty przez najemcę
     * @param settlementDate data rozliczenia
     */
    void updateSettlement(Long rentedObjectId, BigDecimal totalSum, LocalDateTime settlementDate);

}
