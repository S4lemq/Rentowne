package pl.rentowne.settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.settlement.model.Settlement;
import pl.rentowne.settlement.repository.custom.SettlementRepositoryCustom;

/**
 * Repozytorium odczytów stanu licznika
 */
public interface SettlementRepository extends JpaRepository<Settlement, Long>, SettlementRepositoryCustom {
}
