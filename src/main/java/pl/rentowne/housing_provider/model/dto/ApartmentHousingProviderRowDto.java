package pl.rentowne.housing_provider.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;
import pl.rentowne.housing_provider.model.ProviderType;

import java.math.BigDecimal;

@Getter
public class ApartmentHousingProviderRowDto implements DTRow {
    private Long id;
    private String name;
    private ProviderType type;
    private BigDecimal tax;

    @QueryProjection
    public ApartmentHousingProviderRowDto(Long id, String name, ProviderType type, BigDecimal tax) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.tax = tax;
    }
}
