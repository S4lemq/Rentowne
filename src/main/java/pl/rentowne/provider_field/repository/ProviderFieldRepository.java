package pl.rentowne.provider_field.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.provider_field.model.ProviderField;
import pl.rentowne.provider_field.repository.custom.ProviderFieldRepositoryCustom;

/**
 * Repozytorium pól dostawców świadczeń
 */
public interface ProviderFieldRepository extends JpaRepository<ProviderField, Long>, ProviderFieldRepositoryCustom {
}
