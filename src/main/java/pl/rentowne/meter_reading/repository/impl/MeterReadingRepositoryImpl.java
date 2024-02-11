package pl.rentowne.meter_reading.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.meter.model.MeterType;
import pl.rentowne.meter.model.QMeter;
import pl.rentowne.meter.model.dto.QMeterDto;
import pl.rentowne.meter_reading.model.MeterReading;
import pl.rentowne.meter_reading.model.QMeterReading;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;
import pl.rentowne.meter_reading.model.dto.QMeterReadingDto;
import pl.rentowne.meter_reading.repository.custom.MeterReadingRepositoryCustom;

import java.util.List;

@Repository
public class MeterReadingRepositoryImpl extends BaseRepositoryImpl<MeterReading, Long> implements MeterReadingRepositoryCustom {

    private static final QMeterReading meterReading = QMeterReading.meterReading;
    private static final QMeter meter = QMeter.meter;

    public MeterReadingRepositoryImpl(EntityManager entityManager) {
        super(MeterReading.class, entityManager);
    }

    @Override
    public MeterReading getMeterReadingById(Long id) {
        return queryFactory.select(meterReading)
                .from(meterReading)
                .where(meterReading.id.eq(id))
                .fetchOne();
    }

    @Override
    public MeterReading getPreviousMeterReading(MeterReading currentReading, Long meterId) {
        return queryFactory.selectFrom(meterReading)
                .where(meterReading.meter().id.eq(meterId)
                        .and(meterReading.readingDate.lt(currentReading.getReadingDate())))
                .orderBy(meterReading.readingDate.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public MeterReading getMeterReadingByMeter(Long meterId) {
        return queryFactory.select(meterReading)
                .from(meterReading)
                .where(meterReading.meter().id.eq(meterId))
                .orderBy(meterReading.readingDate.desc())
                .limit(1)
                .fetchOne();
    }

    @Override
    public void updateSettled(List<Long> meterReadingIds) {
        queryFactory.update(meterReading)
                .set(meterReading.settled, true)
                .where(meterReading.id.in(meterReadingIds))
                .execute();
    }

    public MeterReadingDto getMeterIdReadingValueAndDate(Long rentedObjectId, MeterType type) {
        return queryFactory
                .select(new QMeterReadingDto(meterReading.currentReading, new QMeterDto(meter.id), meterReading.readingDate))
                .from(meter)
                .leftJoin(meterReading).on(meterReading.meter().id.eq(meter.id))
                .where(meter.meterType.eq(type)
                        .and(meter.rentedObject().id.eq(rentedObjectId)))
                .orderBy(meterReading.readingDate.desc())
                .limit(1)
                .fetchOne();
    }
}
