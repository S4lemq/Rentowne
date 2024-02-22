package pl.rentowne.tenant_settlement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import pl.rentowne.tenant_settlement.repository.custom.TenantSettlementRepositoryCustom;

@Repository
public class TenantSettlementRepositoryImpl extends BaseRepositoryImpl<TenantSettlement, Long> implements TenantSettlementRepositoryCustom {

    public TenantSettlementRepositoryImpl(EntityManager entityManager) {
        super(TenantSettlement.class, entityManager);
    }
}
