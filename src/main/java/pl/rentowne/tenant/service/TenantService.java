package pl.rentowne.tenant.service;

import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.tenant.model.dto.TenantDto;

/**
 * Serwis najemców
 */
public interface TenantService {
    /**
     * Dodaje najemcę wraz z umową najmu
     * @param tenantDto dto najemcy {@link TenantDto}
     * @return id dodanego najemcy
     */
    Long addTenantAndLeaseAgreement(TenantDto tenantDto) throws RentowneBusinessException;

    /**
     * Pobiera najemcę wraz z umową
     * @param id id najemcy
     * @return dto najemcy {@link TenantDto}
     * @throws RentowneNotFoundException wyjatek gdy nie ma takiego najemcy
     */
    TenantDto getTenantAndLeaseAgreement(Long id) throws RentowneNotFoundException;

    /**
     * Metoda służy do aktualizacji danych najemcy oraz umowy
     * @param dto dto najemcy {@link TenantDto}
     */
    void updateTenantAndLeaseAgreement(TenantDto dto);
}
