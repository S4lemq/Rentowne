package pl.rentowne.tenant_settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.tenant_settlement.model.TenantSettlementLog;

/**
 * Repozytorium do powiadomień o statusie płatności
 */
public interface TenantSettlementLogRepository extends JpaRepository<TenantSettlementLog, Long> {
}
