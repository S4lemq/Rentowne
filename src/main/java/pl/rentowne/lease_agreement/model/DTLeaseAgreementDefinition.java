package pl.rentowne.lease_agreement.model;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rentowne.apartment.model.QApartment;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.filter.DTFilter;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.lease_agreement.model.dto.QLeaseAgreementRowDto;
import pl.rentowne.rented_object.model.QRentedObject;
import pl.rentowne.tenant.model.QTenant;
import pl.rentowne.user.service.UserService;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("LEASE_AGREEMENT")
public class DTLeaseAgreementDefinition implements DTDefinition {
    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QApartment apartment = QApartment.apartment;
    private static final QTenant tenant = QTenant.tenant;

    private final UserService userService;

    @Override
    public ConstructorExpression getSelect() {
        return new QLeaseAgreementRowDto(
                tenant.id,
                apartment.apartmentName,
                rentedObject.rentedObjectName,
                leaseAgreement.compensationAmount,
                leaseAgreement.rentAmount,
                leaseAgreement.internetFee,
                leaseAgreement.gasDeposit,
                leaseAgreement.includedWaterMeters,
                leaseAgreement.startContractDate,
                leaseAgreement.endContractDate,
                leaseAgreement.deposit
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return leaseAgreement;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("tenantId", tenant.id, true),
                new DTColumnDefinition("apartment", apartment.apartmentName,
                        jpaQuery -> jpaQuery
                                .join(tenant).on(leaseAgreement.id.eq(tenant.leaseAgreement().id))
                                .join(rentedObject).on(tenant.rentedObject().id.eq(rentedObject.id))
                                .join(apartment).on(rentedObject.apartment().id.eq(apartment.id))),
                new DTColumnDefinition("rentedObject", rentedObject.rentedObjectName),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.compensationAmount, true),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.rentAmount, true),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.internetFee, true),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.gasDeposit, true),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.includedWaterMeters, true),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.startContractDate, true),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.endContractDate, true),
                new DTColumnDefinition("leaseAgreement", leaseAgreement.deposit, true)
        );
    }

    @Override
    public Predicate getMainWherePredicate(List<DTFilter> filters) {
        Long id = null;
        try {
            id = userService.getLoggedUser().getId();
        } catch (RentowneBusinessException e) {
            log.error("user not logged in, cannot download the apartment table");
        }
        return apartment.user().id.eq(id);
    }

    @Override
    public OrderSpecifier[] addBaseOrderBy() {
        return new OrderSpecifier[]{apartment.apartmentName.asc(), rentedObject.rentedObjectName.asc()};
    }
}
