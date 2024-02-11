package pl.rentowne.meter_reading.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.meter_reading.model.MeterReading;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;
import pl.rentowne.meter_reading.repository.MeterReadingRepository;
import pl.rentowne.meter_reading.service.MeterReadingService;
import pl.rentowne.util.DateUtils;

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
            DateUtils.compareDates(dto.getReadingDate(), dto.getPreviousReadingDate(), false);
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



}
