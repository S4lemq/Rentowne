package pl.rentowne.meter_reading.service;

import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;

public interface MeterReadingService {
    /**
     * Dodaje odczyt licznika
     * @param dto odczytu licznika
     * @throws RentowneBusinessException wyjątek biznesowy
     */
    void addMeterReading(MeterReadingDto dto) throws RentowneBusinessException;

    /**
     * Pobiera aktualny i poprzedni stan licznika
     * @param meterId id licznika
     * @param id id odczytu licznika
     * @return odczyt licznika
     */
    MeterReadingDto getCurrentAndPreviousReading(Long meterId, Long id);

    /**
     * Pobiera odczyt licznika po id licznika
     * @param meterId id licznika
     * @return odczyt licznika
     */
    MeterReadingDto getMeterReadingByMeter(Long meterId);
}
