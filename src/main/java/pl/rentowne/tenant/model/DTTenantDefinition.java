package pl.rentowne.tenant.model;

import com.querydsl.core.types.ConstructorExpression;
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
import pl.rentowne.lease_agreement.model.QLeaseAgreement;
import pl.rentowne.rented_object.model.QRentedObject;
import pl.rentowne.tenant.model.dto.QTenantRowDto;
import pl.rentowne.user.service.UserService;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("TENANT")
public class DTTenantDefinition implements DTDefinition {
    private static final QTenant tenant = QTenant.tenant;
    private static final QApartment apartment = QApartment.apartment;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;

    private final UserService userService;


    @Override
    public ConstructorExpression getSelect() {
        return new QTenantRowDto(
                tenant.id,
                tenant.firstname,
                tenant.lastname,
                apartment.apartmentName,
                rentedObject.rentedObjectName,
                tenant.phoneNumber,
                tenant.email,
                leaseAgreement.endContractDate
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return tenant;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("id", tenant.id, true),
                new DTColumnDefinition("firstname", tenant.firstname),
                new DTColumnDefinition("lastname", tenant.lastname),
                new DTColumnDefinition("apartmentName",
                        apartment.apartmentName,
                        jpaQuery -> jpaQuery
                                .join(rentedObject).on(tenant.rentedObject.id.eq(rentedObject.id))
                                .join(apartment).on(rentedObject.apartment().id.eq(apartment.id))),
                new DTColumnDefinition("rentedObjectName",
                        rentedObject.rentedObjectName,
                        jpaQuery -> jpaQuery
                                .join(rentedObject).on(tenant.rentedObject.id.eq(rentedObject.id))),
                new DTColumnDefinition("phoneNumber", tenant.phoneNumber, true),
                new DTColumnDefinition("email", tenant.email),
                new DTColumnDefinition("endContractDate",
                        leaseAgreement.endContractDate,
                        jpaQuery -> jpaQuery
                                .join(leaseAgreement).on(tenant.leaseAgreement.id.eq(leaseAgreement.id)), true)
        );
    }

    @Override
    public Predicate getMainWherePredicate(List<DTFilter> filters) {
        Long id = null;
        try {
            id = userService.getLoggedUser().getId();
        } catch (
                RentowneBusinessException e) {
            log.error("user not logged in, cannot download the tenant table");
        }
        return apartment.user().id.eq(id);
    }

}
