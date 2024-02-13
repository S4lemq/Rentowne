package pl.rentowne.settlement.service;

import java.time.LocalDateTime;

public interface SettlementService {

    /**
     * oblicza poszczególne media po licznikach i danych dostawcy
     * @param rentedObjectId id obiektu do wynajęcia
     * @param waterIncluded czy rozliczyć wodę
     * @param electricityIncluded czy rozliczyć energię elektryczną
     * @param settlementDate data rozliczenia
     */
    void calculate(Long rentedObjectId, boolean waterIncluded, boolean electricityIncluded, LocalDateTime settlementDate);
}
