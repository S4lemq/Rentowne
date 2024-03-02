package pl.rentowne.tenant.repository.custom;

import pl.rentowne.apartment.model.Apartment;
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

    /**
     * Pobiera id obiektu do wynajęcia po id najemcy
     * @param id id najemcy
     * @return id obiektu do wynajęcia
     */
    Long getRentedObjectId(Long id);

    /**
     * Pobiera najemcę
     * @param loggedEmail email najemcy
     * @return {@link Tenant}
     */
    Tenant getTenantByEmail(String loggedEmail);

    /**
     * Pobiera id obiektu gdzie jest najemca
     * @param email email najemcy
     * @return id obiektu do wynajęcia
     */
    Long getRentedObjectIdByTenantEmail(String email);
}
