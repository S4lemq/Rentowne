package pl.rentowne.housing_provider.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.model.ProviderType;
import pl.rentowne.provider_field.model.dto.ProviderFieldDto;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class HousingProviderDto {
    private Long id;
    private String name;
    private ProviderType type;
    private Apartment apartment;
    private Set<ProviderFieldDto> providerFieldDtos;

    public static HousingProvider asEntity(HousingProviderDto dto) {
        HousingProvider provider = HousingProvider.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .apartment(Apartment.builder()
                        .id(dto.getApartment().getId())
                        .build())
                .build();

        provider.setProviderFields(
                dto.getProviderFieldDtos().stream()
                        .map(fieldDto -> ProviderFieldDto.asEntity(fieldDto, provider))
                        .collect(Collectors.toSet())
        );

        return provider;
    }

}


