package pl.rentowne.settlement.repository.custom;

import pl.rentowne.settlement.model.SettlementStatus;
import pl.rentowne.settlement.model.dto.SettlementDateAmountDto;
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

    /**
     * Pobiera dane rozliczeń danego użytkownika
     * @param userId id zalogowanego użytkownika
     * @return lista rozliczeń
     */
    List<SettlementDateAmountDto> getDataForStatistics(Long userId);

    /**
     * Pobiera id ostatniego rozliczenia
     * @param id id najemcy
     * @return id ostatniego rozliczenia
     */
    Long getLastSettlementByTenantId(Long id);

    /**
     * Aktualizuje status rozliczenia
     *
     * @param settlementId     id rozliczenia
     * @param settlementStatus status
     * @param orderHash hash rozliczenia
     */
    void updateStatusAndHash(Long settlementId, SettlementStatus settlementStatus, String orderHash);

    /**
     * Aktualizuje status rozliczenia
     * @param orderHash hash rozliczenia
     * @param settlementStatus status
     */
    void updateStatus(String orderHash, SettlementStatus settlementStatus);
}
