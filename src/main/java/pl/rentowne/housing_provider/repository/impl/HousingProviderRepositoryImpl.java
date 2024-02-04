package pl.rentowne.housing_provider.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.repository.custom.HousingProviderRepositoryCustom;

@Repository
public class HousingProviderRepositoryImpl extends BaseRepositoryImpl<HousingProvider, Long> implements HousingProviderRepositoryCustom {

    public HousingProviderRepositoryImpl(EntityManager entityManager) {
        super(HousingProvider.class, entityManager);
    }
}
