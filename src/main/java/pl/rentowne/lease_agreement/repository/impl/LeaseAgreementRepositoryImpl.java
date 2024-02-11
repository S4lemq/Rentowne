package pl.rentowne.lease_agreement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.lease_agreement.model.LeaseAgreement;
import pl.rentowne.lease_agreement.model.QLeaseAgreement;
import pl.rentowne.lease_agreement.model.dto.LeaseAgreementDto;
import pl.rentowne.lease_agreement.model.dto.QLeaseAgreementDto;
import pl.rentowne.lease_agreement.repository.custom.LeaseAgreementRepositoryCustom;
import pl.rentowne.tenant.model.QTenant;

@Repository
public class LeaseAgreementRepositoryImpl extends BaseRepositoryImpl<LeaseAgreement, Long> implements LeaseAgreementRepositoryCustom {

    private static final QTenant tenant = QTenant.tenant;
    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;
    private static final QLeaseAgreementDto leaseAgreementDto = new QLeaseAgreementDto(leaseAgreement.rentAmount,
            leaseAgreement.compensationAmount, leaseAgreement.internetFee, leaseAgreement.includedWaterMeters, leaseAgreement.gasDeposit);

    public LeaseAgreementRepositoryImpl(EntityManager entityManager) {
        super(LeaseAgreement.class, entityManager);
    }

    @Override
    public LeaseAgreementDto getData(Long rentedObjectId) {
        return queryFactory.select(leaseAgreementDto)
                .from(leaseAgreement)
                .join(tenant).on(leaseAgreement.tenant().id.eq(tenant.id))
                .where(tenant.rentedObject().id.eq(rentedObjectId))
                .fetchOne();
    }
}
