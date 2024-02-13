package pl.rentowne.settlement.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.lease_agreement.model.QLeaseAgreement;
import pl.rentowne.rented_object.model.QRentedObject;
import pl.rentowne.settlement.model.QSettlement;
import pl.rentowne.settlement.model.Settlement;
import pl.rentowne.settlement.model.dto.QSettlementExportDto;
import pl.rentowne.settlement.model.dto.SettlementExportDto;
import pl.rentowne.settlement.repository.custom.SettlementRepositoryCustom;
import pl.rentowne.tenant.model.QTenant;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SettlementRepositoryImpl extends BaseRepositoryImpl<Settlement, Long> implements SettlementRepositoryCustom {

    private static final QTenant tenant = QTenant.tenant;
    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;
    private static final QSettlement settlement = QSettlement.settlement;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QSettlementExportDto dto = new QSettlementExportDto(settlement.settlementDate, leaseAgreement.compensationAmount,
            leaseAgreement.rentAmount, leaseAgreement.internetFee, leaseAgreement.gasDeposit, settlement.electricityAmount,
            settlement.waterAmount, settlement.totalAmount
    );

    public SettlementRepositoryImpl(EntityManager entityManager) {
        super(Settlement.class, entityManager);
    }

    @Override
    public List<SettlementExportDto> getSettlementByDateAndRentedObjectId(LocalDateTime from, LocalDateTime to, Long rentedObjectId) {
        return queryFactory.select(dto)
                .from(settlement)
                .join(rentedObject).on(settlement.rentedObject().id.eq(rentedObject.id))
                .join(tenant).on(rentedObject.id.eq(tenant.rentedObject().id))
                .join(leaseAgreement).on(tenant.leaseAgreement().id.eq(leaseAgreement.id))
                .where(settlement.settlementDate.between(from, to)
                        .and(rentedObject.id.eq(rentedObjectId)))
                .orderBy(settlement.settlementDate.asc())
                .fetch();
    }
}
