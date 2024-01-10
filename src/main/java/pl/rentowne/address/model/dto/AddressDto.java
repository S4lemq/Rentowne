package pl.rentowne.address.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class AddressDto {
    private Long id;
    private String streetName;
    private String buildingNumber;
    private String apartmentNumber;
    private String zipCode;
    private String cityName;
    private String voivodeship;

    @QueryProjection
    public AddressDto(Long id, String streetName, String buildingNumber, String apartmentNumber, String zipCode,
                      String cityName, String voivodeship) {
        this.id = id;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.zipCode = zipCode;
        this.cityName = cityName;
        this.voivodeship = voivodeship;
    }
}
