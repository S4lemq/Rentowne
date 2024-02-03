package pl.rentowne.lease_agreement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.lease_agreement.model.LeaseAgreement;
import pl.rentowne.lease_agreement.repository.custom.LeaseAgreementRepositoryCustom;

@Repository
public class LeaseAgreementRepositoryImpl extends BaseRepositoryImpl<LeaseAgreement, Long> implements LeaseAgreementRepositoryCustom {

    public LeaseAgreementRepositoryImpl(EntityManager entityManager) {
        super(LeaseAgreement.class, entityManager);
    }
}
