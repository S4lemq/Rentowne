package pl.rentowne.meter.repository.impl;


import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.meter.model.Meter;
import pl.rentowne.meter.model.MeterType;
import pl.rentowne.meter.model.QMeter;
import pl.rentowne.meter.repository.cutom.MeterRepositoryCustom;
import pl.rentowne.meter_reading.model.QMeterReading;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;
import pl.rentowne.meter_reading.model.dto.QMeterReadingDto;

import java.util.List;

@Repository
public class MeterRepositoryImpl extends BaseRepositoryImpl<Meter, Long> implements MeterRepositoryCustom {

    private static final QMeter meter = QMeter.meter;
    private static final QMeterReading meterReading = QMeterReading.meterReading;
    private static final QMeterReadingDto meterReadingDto = new QMeterReadingDto(meterReading.id, meterReading.consumption);

    public MeterRepositoryImpl(EntityManager entityManager) {
        super(Meter.class, entityManager);
    }

    @Override
    public List<MeterReadingDto> getConsumptionAndIdByRentedObjectAndMeterType(Long rentedObjectId, MeterType meterType) {
        return queryFactory.select(meterReadingDto)
                .from(meter)
                .join(meterReading).on(meterReading.meter().id.eq(meter.id))
                .where(meter.rentedObject().id.eq(rentedObjectId)
                        .and(meter.meterType.eq(meterType))
                        .and(meterReading.settled.eq(Boolean.FALSE)))
                .fetch();
    }

    @Override
    public List<Long> findMeterCountByRentedObject(Long id) {
        return queryFactory.select(meter.id)
                .from(meter)
                .where(meter.rentedObject().id.eq(id)
                        .and((meter.meterType.eq(MeterType.ELECTRIC).or(meter.meterType.eq(MeterType.WATER_COLD)))))
                .fetch();
    }

}
