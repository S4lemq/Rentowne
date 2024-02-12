package pl.rentowne.rented_object.model;

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
import pl.rentowne.rented_object.model.dto.QRentedObjectSettlementRowDto;
import pl.rentowne.tenant.model.QTenant;
import pl.rentowne.user.service.UserService;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("RENTED_OBJECT_SETTLEMENT")
public class DTRentedObjectSettlementDefinition implements DTDefinition {
    private final UserService userService;

    private static final QApartment apartment = QApartment.apartment;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QTenant tenant = QTenant.tenant;

    @Override
    public ConstructorExpression getSelect() {
        return new QRentedObjectSettlementRowDto(
                rentedObject.id,
                apartment.apartmentName,
                rentedObject.rentedObjectName,
                tenant.firstname,
                tenant.lastname,
                rentedObject.lastSettlementDate,
                rentedObject.lastSettlementTotalAmount
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return apartment;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("rentedObjectId",
                        rentedObject.id,
                        jpaQuery -> jpaQuery
                                .join(rentedObject).on(apartment.id.eq(rentedObject.apartment().id)), true),
                new DTColumnDefinition("apartment", apartment.apartmentName),
                new DTColumnDefinition("rentedObject", rentedObject.rentedObjectName),
                new DTColumnDefinition("tenantName",
                        tenant.firstname,
                        jpaQuery -> jpaQuery
                                .leftJoin(tenant).on(rentedObject.id.eq(tenant.rentedObject().id))),
                new DTColumnDefinition("tenantSurname", tenant.lastname),
                new DTColumnDefinition("lastSettlementDate", rentedObject.lastSettlementDate, true),
                new DTColumnDefinition("lastSettlementTotalAmount", rentedObject.lastSettlementTotalAmount, true)
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

    @Override
    public OrderSpecifier[] addBaseOrderBy() {
        return new OrderSpecifier[]{apartment.apartmentName.asc(), rentedObject.rentedObjectName.asc()};
    }

}
