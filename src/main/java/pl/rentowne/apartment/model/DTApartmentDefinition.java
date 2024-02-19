package pl.rentowne.apartment.model;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.apartment.model.dto.QApartmentRowDto;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.enums.DTFilterType;
import pl.rentowne.data_table.filter.DTFilter;
import pl.rentowne.data_table.filter.DTFilterColumnDefinition;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.user.service.UserService;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("APARTMENT")
public class DTApartmentDefinition implements DTDefinition {

    private final UserService userService;
    private static final QApartment apartment = QApartment.apartment;
    private static final QAddress address = QAddress.address;

    @Override
    public ConstructorExpression getSelect() {
        return new QApartmentRowDto(
                apartment.id,
                apartment.apartmentName,
                apartment.leasesNumber,
                apartment.isRented,
                address.cityName,
                address.streetName,
                address.buildingNumber,
                address.apartmentNumber,
                apartment.image,
                apartment.pinned
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return apartment;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("id", apartment.id, true),
                new DTColumnDefinition("apartmentName", apartment.apartmentName),
                new DTColumnDefinition("leasesNumber", apartment.leasesNumber, true),
                new DTColumnDefinition("isRented", apartment.isRented, true),
                new DTColumnDefinition("cityName", address.cityName,
                        jpaQuery -> jpaQuery
                                .join(address).on(apartment.address.id.eq(address.id))),
                new DTColumnDefinition("streetName", address.streetName),
                new DTColumnDefinition("buildingNumber", address.buildingNumber,true),
                new DTColumnDefinition("apartmentNumber", address.apartmentNumber, true),
                new DTColumnDefinition("pinned", apartment.pinned, true)
        );
    }

    @Override
    public List<DTFilterColumnDefinition> getUserDefinedFilterColumns() {
        return Arrays.asList(
                new DTFilterColumnDefinition("leasesNumber", apartment.leasesNumber, DTFilterType.EQUALS),
                new DTFilterColumnDefinition("isRented", apartment.isRented, DTFilterType.BOOLEAN),
                new DTFilterColumnDefinition("apartmentName", apartment.apartmentName, DTFilterType.START_WITH)
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
        return apartment.user.id.eq(id);
    }

    @Override
    public OrderSpecifier[] addBaseOrderBy() {
        return new OrderSpecifier[]{apartment.pinned.desc(), apartment.id.asc()};
    }
}
