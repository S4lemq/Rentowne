package pl.rentowne.service_provider_field.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.housing_service_provider.model.HousingServiceProvider;
import pl.rentowne.housing_service_provider.model.dto.HousingServiceProviderDto;
import pl.rentowne.service_provider_field.model.BillingMethod;
import pl.rentowne.service_provider_field.model.ServiceProviderField;

import java.math.BigDecimal;

@Getter
@Builder
public class ServiceProviderFieldDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private BillingMethod billingMethod;
    private BigDecimal tax;
    private HousingServiceProviderDto housingServiceProviderDto;


    public static ServiceProviderField asEntity(ServiceProviderFieldDto dto, HousingServiceProvider housingServiceProvider) {
        ServiceProviderField field = ServiceProviderField.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .billingMethod(dto.getBillingMethod())
                .tax(dto.getTax())
                .housingServiceProvider(housingServiceProvider)
                .build();

        housingServiceProvider.getServiceProviderFields().add(field);
        return field;
    }
}
