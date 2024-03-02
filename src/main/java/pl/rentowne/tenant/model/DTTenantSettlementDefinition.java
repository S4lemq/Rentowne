package pl.rentowne.tenant.model;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.filter.DTFilter;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.settlement.model.QSettlement;
import pl.rentowne.tenant.model.dto.QTenantSettlementRowDto;
import pl.rentowne.user.service.UserService;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("TENANT_SETTLEMENT")
public class DTTenantSettlementDefinition implements DTDefinition {

    private static final QSettlement settlement = QSettlement.settlement;
    private final UserService userService;

    @Override
    public ConstructorExpression getSelect() {
        return new QTenantSettlementRowDto(
                settlement.id,
                settlement.settlementDate,
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
                new DTColumnDefinition("id", settlement.id, true),
                new DTColumnDefinition("settlementDate", settlement.settlementDate, true),
                new DTColumnDefinition("totalAmount", settlement.totalAmount, true)
        );
    }

    @Override
    public Predicate getMainWherePredicate(List<DTFilter> filters) {
        Long rentedObjectId = null;
        try {
            rentedObjectId = userService.getRentedObjectByLoggedTenant();
        } catch (RentowneBusinessException e) {
            log.error("user not logged in, cannot download the settlement table");
        }
        return settlement.rentedObject().id.eq(rentedObjectId);
    }

    @Override
    public OrderSpecifier[] addBaseOrderBy() {
        return new OrderSpecifier[]{settlement.id.desc()};
    }


}
