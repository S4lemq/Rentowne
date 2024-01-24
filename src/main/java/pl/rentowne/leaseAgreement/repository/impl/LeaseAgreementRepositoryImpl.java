package pl.rentowne.leaseAgreement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;
import pl.rentowne.leaseAgreement.model.QLeaseAgreement;
import pl.rentowne.leaseAgreement.repository.custom.LeaseAgreementRepositoryCustom;

import java.util.Optional;

@Repository
public class LeaseAgreementRepositoryImpl extends BaseRepositoryImpl<LeaseAgreement, Long> implements LeaseAgreementRepositoryCustom {

    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;

    public LeaseAgreementRepositoryImpl(EntityManager entityManager) {
        super(LeaseAgreement.class, entityManager);
    }

    @Override
    public Optional<LeaseAgreement> getLeaseById(Long id) {
        return Optional.ofNullable(
                queryFactory.select(leaseAgreement)
                        .from(leaseAgreement)
                        .where(leaseAgreement.id.eq(id))
                        .fetchOne()
        );
    }
}
