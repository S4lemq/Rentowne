package pl.rentowne.housing_service_provider.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.housing_service_provider.model.dto.HousingServiceProviderDto;
import pl.rentowne.housing_service_provider.repository.HousingServiceProviderRepository;
import pl.rentowne.housing_service_provider.service.HousingServiceProviderService;

@Service
@RequiredArgsConstructor
public class HousingServiceProviderServiceImpl implements HousingServiceProviderService {

    private final HousingServiceProviderRepository housingServiceProviderRepository;

    @Override
    @Transactional
    public void addHousingServiceProvider(HousingServiceProviderDto dto) {
        housingServiceProviderRepository.save(HousingServiceProviderDto.asEntity(dto));
    }

    @Override
    public HousingServiceProviderDto getHousingProviderById(Long id) {

        return null;
    }
}
