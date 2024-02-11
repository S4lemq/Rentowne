package pl.rentowne.meter_reading.repository.custom;

import pl.rentowne.meter.model.MeterType;
import pl.rentowne.meter_reading.model.MeterReading;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Rozszerzone repozytorium odczytów stanu licznika
 */
public interface MeterReadingRepositoryCustom {

    /**
     * Pobiera stan licznika po id stanu licznika
     * @param id id stanu licznika
     * @return odczyt licznika
     */
    MeterReading getMeterReadingById(Long id);

    /**
     * Pobiera poprzedni stan licznika po aktualnym odczycie i id licznika
     * @param currentReading aktualny odczyt licznika
     * @param meterId id licznika
     * @return odczyt licznika
     */
    MeterReading getPreviousMeterReading(MeterReading currentReading, Long meterId);

    /**
     * Pobiera odczyt licznika po id licznika
     * @param meterId id licznika
     * @return odczyt licznika
     */
    MeterReading getMeterReadingByMeter(Long meterId);

    /**
     * Aktualizuje flagę czy rozliczono na odczytach liczników
     * @param meterReadingIds id odczytów liczników
     */
    void updateSettled(List<Long> meterReadingIds);

    /**
     * Pobiera id licznika, odczyt oraz datę odczytu
     * @param rentedObjectId id obiektu do wynajęcia
     * @param type typ licznika
     * @return id licznika, odczyt oraz data odczytu
     */
    MeterReadingDto getMeterIdReadingValueAndDate(Long rentedObjectId, MeterType type);
}
