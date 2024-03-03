package pl.rentowne.meter_reading.model;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.enums.DTFilterType;
import pl.rentowne.data_table.enums.DTJoinType;
import pl.rentowne.data_table.filter.DTFilterColumnDefinition;
import pl.rentowne.meter.model.QMeter;
import pl.rentowne.meter_reading.model.dto.QMeterReadingRowDto;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Component("METER_READING")
public class DTMeterReadingDefinition implements DTDefinition {

    private static final QMeterReading meterReading = QMeterReading.meterReading;
    private static final QMeter meter = QMeter.meter;

    @Override
    public ConstructorExpression getSelect() {
        return new QMeterReadingRowDto(
                meterReading.id,
                meterReading.currentReading,
                meterReading.readingDate,
                meterReading.consumption,
                meterReading.settled,
                meter.meterType
        );
    }

    @Override
    public EntityPathBase getEntity() {
        return meterReading;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return Arrays.asList(
                new DTColumnDefinition("id", meterReading.id),
                new DTColumnDefinition("currentReading", meterReading.currentReading),
                new DTColumnDefinition("readingDate", meterReading.readingDate),
                new DTColumnDefinition("consumption", meterReading.consumption),
                new DTColumnDefinition("settled", meterReading.settled),
                new DTColumnDefinition("meterType",
                        meter.meterType,
                        jpaQuery -> jpaQuery
                            .join(meter).on(meterReading.meter.id.eq(meter.id)))
        );
    }

    @Override
    public List<DTFilterColumnDefinition> getUserDefinedFilterColumns() {
        return List.of(
                new DTFilterColumnDefinition(
                        "meterId",
                        meter,
                        meter.id,
                        meterReading.meter().id.eq(meter.id),
                        DTJoinType.INNER_JOIN,
                        DTFilterType.EQUALS
                )
        );
    }

    @Override
    public OrderSpecifier[] addBaseOrderBy() {
        return new OrderSpecifier[] {
                meterReading.readingDate.desc()
        };
    }
}
