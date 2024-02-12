package pl.rentowne.settlement;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.filter.DTFilterColumnDefinition;
import pl.rentowne.lease_agreement.model.QLeaseAgreement;
import pl.rentowne.rented_object.model.QRentedObject;
import pl.rentowne.settlement.model.QSettlement;
import pl.rentowne.settlement.model.dto.QSettlementRowDto;
import pl.rentowne.tenant.model.QTenant;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("SETTLEMENT")
public class DTSettlement implements DTDefinition {

    private static final QTenant tenant = QTenant.tenant;
    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;
    private static final QSettlement settlement = QSettlement.settlement;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;

    @Override
    public ConstructorExpression getSelect() {
        return new QSettlementRowDto(
                settlement.insertDate,
                leaseAgreement.compensationAmount,
                leaseAgreement.rentAmount,
                leaseAgreement.internetFee,
                leaseAgreement.gasDeposit,
                settlement.electricityAmount,
                settlement.waterAmount,
                settlement.totalAmount
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return settlement;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("date", settlement.insertDate),
                new DTColumnDefinition("compensationAmount",
                        leaseAgreement.compensationAmount,
                        jpaQuery -> jpaQuery
                                .join(rentedObject).on(settlement.rentedObject().id.eq(rentedObject.id))
                                .join(tenant).on(rentedObject.id.eq(tenant.rentedObject().id))
                                .join(leaseAgreement).on(tenant.leaseAgreement().id.eq(leaseAgreement.id))),
                new DTColumnDefinition("rentAmount", leaseAgreement.rentAmount),
                new DTColumnDefinition("internetFee", leaseAgreement.internetFee),
                new DTColumnDefinition("gasDeposit", leaseAgreement.gasDeposit),
                new DTColumnDefinition("electricityAmount", settlement.electricityAmount),
                new DTColumnDefinition("waterAmount", settlement.waterAmount),
                new DTColumnDefinition("totalAmount", settlement.totalAmount)
        );
    }

    @Override
    public List<DTFilterColumnDefinition> getUserDefinedFilterColumns() {
        return List.of(
                new DTFilterColumnDefinition(
                        "rentedObjectId",
                        settlement.rentedObject().id
                )
        );
    }
}
