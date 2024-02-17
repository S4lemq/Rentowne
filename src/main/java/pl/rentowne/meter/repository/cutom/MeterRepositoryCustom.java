package pl.rentowne.meter.repository.cutom;

import pl.rentowne.meter.model.MeterType;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;

import java.util.List;

/**
 * Rozszerzone repozytorium liczników
 */
public interface MeterRepositoryCustom {

    /**
     * Pobiera ostatni stan i id licznika
     * @param rentedObjectId id obiektu
     * @param meterType typ licznika
     * @return ostatni stan i id licznika
     */
    List<MeterReadingDto> getConsumptionAndIdByRentedObjectAndMeterType(Long rentedObjectId, MeterType meterType);

    /**
     * Pobiera id liczników danego obiektu do wynajęcia
     * @param id obiektu do wynajęcia
     * @return lista id liczników
     */
    List<Long> findMeterCountByRentedObject(Long id);
}
