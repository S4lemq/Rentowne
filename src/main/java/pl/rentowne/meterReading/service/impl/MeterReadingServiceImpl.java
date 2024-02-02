package pl.rentowne.meterReading.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.meterReading.model.MeterReading;
import pl.rentowne.meterReading.model.dto.MeterReadingDto;
import pl.rentowne.meterReading.repository.MeterReadingRepository;
import pl.rentowne.meterReading.service.MeterReadingService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class MeterReadingServiceImpl implements MeterReadingService {

    private final MeterReadingRepository meterReadingRepository;

    @Transactional
    @Override
    public void addMeterReading(MeterReadingDto dto) throws RentowneBusinessException {
        if (dto.getPreviousReading() != null && dto.getPreviousReadingDate() != null) {
            if (dto.getCurrentReading().compareTo(dto.getPreviousReading()) <= 0) {
                throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_VALUES);
            }
            this.compareDates(dto.getReadingDate(), dto.getPreviousReadingDate());
        }

        meterReadingRepository.save(MeterReadingDto.asEntity(dto));
    }

    @Override
    public MeterReadingDto getCurrentAndPreviousReading(Long meterId, Long id) {

        MeterReading currentReading = meterReadingRepository.getMeterReadingById(id);
        MeterReading previousReading = meterReadingRepository.getPreviousMeterReading(currentReading, meterId);

        return MeterReadingDto.builder()
                .id(currentReading.getId())
                .currentReading(currentReading.getCurrentReading())
                .readingDate(currentReading.getReadingDate())
                .consumption(currentReading.getCurrentReading().subtract(previousReading.getCurrentReading()))
                .previousReading(previousReading.getCurrentReading())
                .previousReadingDate(previousReading.getReadingDate())
                .build();
    }

    @Override
    public MeterReadingDto getMeterReadingByMeter(Long meterId) {
        MeterReading meterReading = meterReadingRepository.getMeterReadingByMeter(meterId);
        if (meterReading == null) return null;
        return MeterReadingDto.builder()
                .id(meterReading.getId())
                .currentReading(meterReading.getCurrentReading())
                .readingDate(meterReading.getReadingDate())
                .build();
    }

    public void compareDates(LocalDateTime readingDate, LocalDateTime previousReadingDate) throws RentowneBusinessException {
        if (readingDate.isBefore(previousReadingDate) || readingDate.isEqual(previousReadingDate)) {
            throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_DATE);
        }

        long monthsBetween = ChronoUnit.MONTHS.between(
                previousReadingDate.withDayOfMonth(1),
                readingDate.withDayOfMonth(1));
        long yearsBetween = ChronoUnit.YEARS.between(
                previousReadingDate.withDayOfMonth(1),
                readingDate.withDayOfMonth(1));

        if (monthsBetween != 1 || yearsBetween > 0) {
            throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_DATE);
        }
    }

}
