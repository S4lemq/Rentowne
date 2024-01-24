package pl.rentowne.tenant.repository.custom;

import pl.rentowne.tenant.model.Tenant;

import java.util.Optional;

/**
 * Rozszerzone repozytorium najemców
 */
public interface TenantRepositoryCustom {
    /**
     * Pobiera Optional'a encji najemcy
     * @param id id najemcy
     * @return Optional encji najemcy
     */
    Optional<Tenant> findByTenantId(Long id);

}
