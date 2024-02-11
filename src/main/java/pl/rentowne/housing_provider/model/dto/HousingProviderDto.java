package pl.rentowne.housing_provider.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.housing_provider.model.ProviderType;
import pl.rentowne.provider_field.model.dto.ProviderFieldDto;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class HousingProviderDto {
    private Long id;
    private String name;
    private ProviderType type;
    private BigDecimal tax;
    private BigDecimal conversionRate;
    private List<ProviderFieldDto> providerFieldDtos;
}


