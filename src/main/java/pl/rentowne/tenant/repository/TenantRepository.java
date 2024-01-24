package pl.rentowne.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.repository.custom.TenantRepositoryCustom;

/**
 * Repozytorium najemców
 */
public interface TenantRepository extends JpaRepository<Tenant, Long>, TenantRepositoryCustom {
}
