package pl.rentowne.leaseAgreement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;
import pl.rentowne.leaseAgreement.repository.custom.LeaseAgreementRepositoryCustom;

@Repository
public class LeaseAgreementRepositoryImpl extends BaseRepositoryImpl<LeaseAgreement, Long> implements LeaseAgreementRepositoryCustom {

    public LeaseAgreementRepositoryImpl(EntityManager entityManager) {
        super(LeaseAgreement.class, entityManager);
    }
}
