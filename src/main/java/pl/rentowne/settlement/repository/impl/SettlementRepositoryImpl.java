package pl.rentowne.settlement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.settlement.model.Settlement;
import pl.rentowne.settlement.repository.custom.SettlementRepositoryCustom;

@Repository
public class SettlementRepositoryImpl extends BaseRepositoryImpl<Settlement, Long> implements SettlementRepositoryCustom {

    public SettlementRepositoryImpl(EntityManager entityManager) {
        super(Settlement.class, entityManager);
    }
}
