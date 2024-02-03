package pl.rentowne.meter_reading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.meter_reading.model.MeterReading;
import pl.rentowne.meter_reading.repository.custom.MeterReadingRepositoryCustom;

/**
 * Repozytorium odczytów stanu licznika
 */
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long>, MeterReadingRepositoryCustom {
}
