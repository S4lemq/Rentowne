package pl.rentowne.housing_provider.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.repository.custom.HousingProviderRepositoryCustom;

/**
 * Repozytorium dostawców świadczeń
 */
public interface HousingProviderRepository extends JpaRepository<HousingProvider, Long>, HousingProviderRepositoryCustom {
}
