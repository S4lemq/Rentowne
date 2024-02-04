package pl.rentowne.provider_field.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.provider_field.model.ProviderField;
import pl.rentowne.provider_field.repository.custom.ProviderFieldRepositoryCustom;

@Service
public class ProviderFieldRepositoryImpl extends BaseRepositoryImpl<ProviderField, Long> implements ProviderFieldRepositoryCustom {
    public ProviderFieldRepositoryImpl(EntityManager entityManager) {
        super(ProviderField.class, entityManager);
    }
}
