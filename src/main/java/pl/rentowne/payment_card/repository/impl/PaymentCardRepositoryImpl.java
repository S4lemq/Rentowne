package pl.rentowne.payment_card.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.payment_card.model.PaymentCard;
import pl.rentowne.payment_card.repository.custom.PaymentCardRepositoryCustom;

@Repository
public class PaymentCardRepositoryImpl extends BaseRepositoryImpl<PaymentCard, Long> implements PaymentCardRepositoryCustom {

    public PaymentCardRepositoryImpl(EntityManager entityManager) {
        super(PaymentCard.class, entityManager);
    }
}
