package pl.rentowne.meter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.rentowne.meter.model.Meter;
import pl.rentowne.meter.repository.cutom.MeterRepositoryCustom;

/**
 * Repozytorium liczników
 */
public interface MeterRepository extends JpaRepository<Meter, Long>, MeterRepositoryCustom {

    @Query("SELECT meter FROM Meter meter LEFT JOIN FETCH meter.rentedObject WHERE meter.id = :id")
    Meter getMeterDtoById(@Param("id") Long id);

}
