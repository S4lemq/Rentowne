package pl.rentowne.tenant.service;

import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.tenant.model.dto.TenantDto;

/**
 * Serwis najemców
 */
public interface TenantService {
    /**
     * Dodaje najemcję
     * @param tenantDto dto najemcy {@link TenantDto}
     * @return id dodanego najemcy
     */
    Long addTenant(TenantDto tenantDto) throws RentowneNotFoundException;

    /**
     * Pobiera najemcę wraz z umową oraz adresem
     * @param id id najemcy
     * @return dto najemcy {@link TenantDto}
     * @throws RentowneNotFoundException wyjatek gdy nie ma takiego najemcy
     */
    TenantDto getTenant(Long id) throws RentowneNotFoundException;
}
