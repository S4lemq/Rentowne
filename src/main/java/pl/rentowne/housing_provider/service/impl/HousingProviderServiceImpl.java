package pl.rentowne.housing_provider.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.housing_provider.model.dto.HousingProviderDto;
import pl.rentowne.housing_provider.repository.HousingProviderRepository;
import pl.rentowne.housing_provider.service.HousingProviderService;

@Service
@RequiredArgsConstructor
public class HousingProviderServiceImpl implements HousingProviderService {

    private final HousingProviderRepository housingProviderRepository;

    @Override
    @Transactional
    public void addHousingServiceProvider(HousingProviderDto dto) {
        housingProviderRepository.save(HousingProviderDto.asEntity(dto));
    }

    @Override
    public HousingProviderDto getHousingProviderById(Long id) {

        return null;
    }
}
