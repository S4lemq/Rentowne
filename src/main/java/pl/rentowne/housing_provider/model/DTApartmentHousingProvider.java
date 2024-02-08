package pl.rentowne.housing_provider.model;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rentowne.apartment.model.QApartment;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.enums.DTFilterType;
import pl.rentowne.data_table.filter.DTFilter;
import pl.rentowne.data_table.filter.DTFilterColumnDefinition;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.housing_provider.model.dto.QApartmentHousingProviderRowDto;
import pl.rentowne.user.model.QUser;
import pl.rentowne.user.service.UserService;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("APARTMENT_HOUSING_PROVIDER")
public class DTApartmentHousingProvider implements DTDefinition {

    private static final QHousingProvider housingProvider = QHousingProvider.housingProvider;
    private static final QApartment apartment = QApartment.apartment;
    private static final QUser user = QUser.user;

    private final UserService userService;

    @Override
    public ConstructorExpression getSelect() {
        return new QApartmentHousingProviderRowDto(
                housingProvider.id,
                housingProvider.name,
                housingProvider.type,
                housingProvider.tax
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return user;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("id", housingProvider.id, jpaQuery -> jpaQuery.join(user.housingProviders, housingProvider)),
                new DTColumnDefinition("name", housingProvider.name),
                new DTColumnDefinition("type", housingProvider.type),
                new DTColumnDefinition("tax", housingProvider.tax)
        );
    }

    @Override
    public List<DTFilterColumnDefinition> getUserDefinedFilterColumns() {
        return List.of(
                new DTFilterColumnDefinition(
                        "apartmentId",
                        apartment.id,
                        query -> query.join(housingProvider.apartments, apartment),
                        DTFilterType.EQUALS
                )
        );
    }

    @Override
    public Predicate getMainWherePredicate(List<DTFilter> filters) {
        Long id;
        try {
            id = userService.getLoggedUser().getId();
        } catch (RentowneBusinessException e) {
            log.error("User not logged in, cannot download the apartment table");
            return null;
        }
        return user.id.eq(id);

    }

}
