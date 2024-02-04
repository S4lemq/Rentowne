package pl.rentowne.provider_field.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.model.dto.HousingProviderDto;
import pl.rentowne.provider_field.model.BillingMethod;
import pl.rentowne.provider_field.model.ProviderField;

import java.math.BigDecimal;

@Getter
@Builder
public class ProviderFieldDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private BillingMethod billingMethod;
    private BigDecimal tax;
    private HousingProviderDto housingProviderDto;


    public static ProviderField asEntity(ProviderFieldDto dto, HousingProvider housingProvider) {
        ProviderField field = ProviderField.builder()
                .id(dto.getId())
                .name(dto.getName())
                .price(dto.getPrice())
                .billingMethod(dto.getBillingMethod())
                .tax(dto.getTax())
                .housingProvider(housingProvider)
                .build();

        housingProvider.getProviderFields().add(field);
        return field;
    }
}
