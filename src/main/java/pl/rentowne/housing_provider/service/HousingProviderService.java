package pl.rentowne.housing_provider.service;

import pl.rentowne.housing_provider.model.dto.HousingProviderDto;

public interface HousingProviderService {
    /**
     * Dodaje dostawcę świadczeń wraz z polami
     *
     * @param dto dostawca świadczeń
     * @return
     */
    Long addHousingServiceProvider(HousingProviderDto dto);

    /**
     * Zwraca dostawcę świadczeń po id
     * @param id id dostawcy świadczeń
     * @return dto dostawcy świadczeń wraz z jego polami
     */
    HousingProviderDto getHousingProviderById(Long id);

    /**
     * Aktualizuje dane dostawców
     * @param dto dto dostawcy
     */
    void updateHousingProvider(HousingProviderDto dto);
}
