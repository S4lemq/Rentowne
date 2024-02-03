package pl.rentowne.service_provider_field.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.service_provider_field.model.ServiceProviderField;
import pl.rentowne.service_provider_field.repository.custom.ServiceProviderFieldRepositoryCustom;

@Service
public class ServiceProviderFieldRepositoryImpl extends BaseRepositoryImpl<ServiceProviderField, Long> implements ServiceProviderFieldRepositoryCustom {
    public ServiceProviderFieldRepositoryImpl(EntityManager entityManager) {
        super(ServiceProviderField.class, entityManager);
    }
}
