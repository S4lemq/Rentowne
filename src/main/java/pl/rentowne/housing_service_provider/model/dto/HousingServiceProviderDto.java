package pl.rentowne.housing_service_provider.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.housing_service_provider.model.HousingServiceProvider;
import pl.rentowne.housing_service_provider.model.ProviderType;
import pl.rentowne.service_provider_field.model.dto.ServiceProviderFieldDto;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class HousingServiceProviderDto {
    private Long id;
    private String name;
    private ProviderType type;
    private Apartment apartment;
    private Set<ServiceProviderFieldDto> serviceProviderFieldDtos;

    public static HousingServiceProvider asEntity(HousingServiceProviderDto dto) {
        HousingServiceProvider provider = HousingServiceProvider.builder()
                .id(dto.getId())
                .name(dto.getName())
                .type(dto.getType())
                .apartment(Apartment.builder()
                        .id(dto.getApartment().getId())
                        .build())
                .build();

        provider.setServiceProviderFields(
                dto.getServiceProviderFieldDtos().stream()
                        .map(fieldDto -> ServiceProviderFieldDto.asEntity(fieldDto, provider))
                        .collect(Collectors.toSet())
        );

        return provider;
    }

}


