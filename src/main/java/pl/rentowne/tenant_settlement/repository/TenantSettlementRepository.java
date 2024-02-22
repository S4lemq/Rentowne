package pl.rentowne.tenant_settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import pl.rentowne.tenant_settlement.repository.custom.TenantSettlementRepositoryCustom;

/**
 * Repozytorium rozliczeń najemcy
 */
public interface TenantSettlementRepository extends JpaRepository<TenantSettlement, Long>, TenantSettlementRepositoryCustom {
}
