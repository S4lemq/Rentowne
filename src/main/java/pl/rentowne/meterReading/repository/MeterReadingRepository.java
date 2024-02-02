package pl.rentowne.meterReading.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.meterReading.model.MeterReading;
import pl.rentowne.meterReading.repository.custom.MeterReadingRepositoryCustom;

/**
 * Repozytorium odczytów stanu licznika
 */
public interface MeterReadingRepository extends JpaRepository<MeterReading, Long>, MeterReadingRepositoryCustom {
}
