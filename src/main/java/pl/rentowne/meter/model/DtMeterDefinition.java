package pl.rentowne.meter.model;

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
import pl.rentowne.meter.model.dto.QMeterRowDto;
import pl.rentowne.rented_object.model.QRentedObject;
import pl.rentowne.user.service.UserService;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component("METER")
public class DtMeterDefinition implements DTDefinition {

    private static final QMeter meter = QMeter.meter;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QApartment apartment = QApartment.apartment;

    private final UserService userService;

    @Override
    public ConstructorExpression getSelect() {
        return new QMeterRowDto(
            meter.id,
            apartment.apartmentName,
            rentedObject.rentedObjectName,
            meter.name,
            meter.meterType,
            meter.meterNumber,
            meter.initialMeterReading,
            meter.installationDate
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return meter;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("id", meter.id, true),
                new DTColumnDefinition("apartment", apartment.apartmentName,
                        jpaQuery -> jpaQuery
                                .join(rentedObject).on(meter.rentedObject.id.eq(rentedObject.id))
                                .join(apartment).on(rentedObject.apartment().id.eq(apartment.id))),
                new DTColumnDefinition("rentedObject", rentedObject.rentedObjectName),
                new DTColumnDefinition("name", meter.name),
                new DTColumnDefinition("meterType", meter.meterType),
                new DTColumnDefinition("meterNumber", meter.meterNumber),
                new DTColumnDefinition("initialMeterReading", meter.initialMeterReading, true),
                new DTColumnDefinition("installationDate", meter.installationDate, true)
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
