package pl.rentowne.housing_provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.repository.custom.HousingProviderRepositoryCustom;

/**
 * Repozytorium dostawców świadczeń
 */
public interface HousingProviderRepository extends JpaRepository<HousingProvider, Long>, HousingProviderRepositoryCustom {

    @Query("SELECT hp FROM HousingProvider hp LEFT JOIN FETCH hp.providerFields WHERE hp.id = :id")
    HousingProvider getHousingById(@Param("id")Long id);
}
