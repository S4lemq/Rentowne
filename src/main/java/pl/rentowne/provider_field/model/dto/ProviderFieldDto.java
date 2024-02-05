package pl.rentowne.provider_field.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.rentowne.housing_provider.model.dto.HousingProviderDto;
import pl.rentowne.provider_field.model.BillingMethod;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ProviderFieldDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private BillingMethod billingMethod;
    private HousingProviderDto housingProviderDto;
}
