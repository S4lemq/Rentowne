package pl.rentowne.housing_service_provider.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.housing_service_provider.model.HousingServiceProvider;
import pl.rentowne.housing_service_provider.repository.custom.HousingServiceProviderRepositoryCustom;

@Repository
public class HousingServiceProviderRepositoryImpl extends BaseRepositoryImpl<HousingServiceProvider, Long> implements HousingServiceProviderRepositoryCustom {

    public HousingServiceProviderRepositoryImpl(EntityManager entityManager) {
        super(HousingServiceProvider.class, entityManager);
    }
}
