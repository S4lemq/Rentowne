package pl.rentowne.tenant_settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.tenant_settlement.model.Payment;
import pl.rentowne.tenant_settlement.repository.custom.PaymentRepositoryCustom;

/**
 * Repozytorium typów płatności
 */
public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentRepositoryCustom {
}
