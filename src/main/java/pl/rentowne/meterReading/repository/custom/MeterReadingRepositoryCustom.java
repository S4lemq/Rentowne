package pl.rentowne.meterReading.repository.custom;

import pl.rentowne.meterReading.model.MeterReading;

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

}
