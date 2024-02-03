package pl.rentowne.housing_service_provider.service;

import pl.rentowne.housing_service_provider.model.dto.HousingServiceProviderDto;

public interface HousingServiceProviderService {
    /**
     * Dodaje dostawcę świadczeń wraz z polami
     * @param dto dostawca świadczeń
     */
    void addHousingServiceProvider(HousingServiceProviderDto dto);

    /**
     * Zwraca dostawcę świadczeń po id
     * @param id id dostawcy świadczeń
     * @return dto dostawcy świadczeń wraz z jego polami
     */
    HousingServiceProviderDto getHousingProviderById(Long id);
}
