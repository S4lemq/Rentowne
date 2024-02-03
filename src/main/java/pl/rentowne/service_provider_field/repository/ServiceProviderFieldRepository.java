package pl.rentowne.service_provider_field.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.service_provider_field.model.ServiceProviderField;
import pl.rentowne.service_provider_field.repository.custom.ServiceProviderFieldRepositoryCustom;

/**
 * Repozytorium pól dostawców świadczeń
 */
public interface ServiceProviderFieldRepository  extends JpaRepository<ServiceProviderField, Long>, ServiceProviderFieldRepositoryCustom {
}
