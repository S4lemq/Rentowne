package pl.rentowne.settlement.service;

import pl.rentowne.rented_object.model.dto.CalculateRequestDto;

public interface SettlementService {

    /**
     * oblicza poszczególne media po licznikach i danych dostawcy
     * @param rentedObjectId id obiektu do wynajęcia
     * @param dto informacje:
     * czy rozliczyć wodę
     * czy rozliczyć energię elektryczną
     * data rozliczenia
     */
    void calculate(Long rentedObjectId, CalculateRequestDto dto);
}
