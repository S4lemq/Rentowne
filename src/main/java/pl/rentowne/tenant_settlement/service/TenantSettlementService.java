package pl.rentowne.tenant_settlement.service;

import pl.rentowne.tenant_settlement.model.dto.NotificationReceiveDto;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementDto;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementSummary;

public interface TenantSettlementService {
    /**
     * Rejestracja transakcji
     * @param dto {@link TenantSettlementDto}
     * @return {@link TenantSettlementSummary}
     */
    TenantSettlementSummary placeSettlement(TenantSettlementDto dto);

    /**
     * Pobiera rozliczenie po hashu transakcji
     * @param orderHash hash transakcji
     * @return encja zamówienia
     */
    TenantSettlement getTenantSettlementByOrderHash(String orderHash);

    /**
     * Pobiera potwierdzenie transakcji
     *
     * @param orderHash  hash transakcji
     * @param receiveDto {@link NotificationReceiveDto}
     * @param remoteAddr adres serwera, z którego przychodzi zapytanie
     */
    void receiveNotification(String orderHash, NotificationReceiveDto receiveDto, String remoteAddr);
}
