package pl.rentowne.housing_provider.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.model.dto.HousingProviderDto;
import pl.rentowne.housing_provider.repository.HousingProviderRepository;
import pl.rentowne.housing_provider.service.HousingProviderService;
import pl.rentowne.provider_field.model.ProviderField;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HousingProviderServiceImpl implements HousingProviderService {

    private final HousingProviderRepository housingProviderRepository;

    @Override
    @Transactional
    public void addHousingServiceProvider(HousingProviderDto dto) {
        housingProviderRepository.save(this.asEntity(dto));
    }

    private HousingProvider asEntity(HousingProviderDto dto) {
        HousingProvider housingProvider = HousingProvider.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .tax(dto.getTax())
                .build();

        List<ProviderField> fields = dto.getProviderFieldDtos().stream()
                .map(field -> ProviderField.builder()
                        .id(field.getId())
                        .name(field.getName())
                        .price(field.getPrice())
                        .billingMethod(field.getBillingMethod())
                        .housingProvider(housingProvider)
                        .build())
                        .toList();

        housingProvider.setProviderFields(fields);
        return housingProvider;
    }

    @Override
    public HousingProviderDto getHousingProviderById(Long id) {

        return null;
    }
}
