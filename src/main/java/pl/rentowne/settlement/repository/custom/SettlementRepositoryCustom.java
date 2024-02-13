package pl.rentowne.settlement.repository.custom;

import pl.rentowne.settlement.model.dto.SettlementExportDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Rozszerzone repozytorium odczytów stanu licznika
 */
public interface SettlementRepositoryCustom {
    /**
     * pobiera dane rozliczenia obiektu
     * @param from od podanej daty
     * @param to do podanej daty
     * @param rentedObjectId id obiektu do wynajęcia
     * @return zwraca
     */
    List<SettlementExportDto> getSettlementByDateAndRentedObjectId(LocalDateTime from, LocalDateTime to, Long rentedObjectId);
}
