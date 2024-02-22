package pl.rentowne.tenant_settlement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.tenant_settlement.model.Payment;
import pl.rentowne.tenant_settlement.repository.custom.PaymentRepositoryCustom;

@Repository
public class PaymentRepositoryImpl extends BaseRepositoryImpl<Payment, Long> implements PaymentRepositoryCustom {

    public PaymentRepositoryImpl(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }
}
