package pl.rentowne.meter.service;

import pl.rentowne.meter.model.dto.MeterDto;

/**
 * Serwis liczników
 */
public interface MeterService {
    /**
     * Dodaje licznik
     * @param meter dto licznika {@link MeterDto}
     * @return zapisane dto licznika
     */
    MeterDto addMeter(MeterDto meter);

    /**
     * Pobiera dto licznika po jego id
     * @param id id licznika
     * @return dto licznika
     */
    MeterDto getMeterById(Long id);

    /**
     * Aktualizuje dane licznika
     * @param meter dto licznika
     */
    void updateMeter(MeterDto meter);

    /**
     *
     * Pobiera ilość wymaganych liczników dla danego obiektu
     * @param id obiektu
     */
    void getMeterCountByRentedObject(Long id);
}
