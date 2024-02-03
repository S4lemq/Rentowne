package pl.rentowne.housing_service_provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.housing_service_provider.model.HousingServiceProvider;
import pl.rentowne.housing_service_provider.repository.custom.HousingServiceProviderRepositoryCustom;

/**
 * Repozytorium dostawców świadczeń
 */
public interface HousingServiceProviderRepository extends JpaRepository<HousingServiceProvider, Long>, HousingServiceProviderRepositoryCustom {
}
