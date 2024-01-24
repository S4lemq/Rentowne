package pl.rentowne.address.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import pl.rentowne.address.model.Address;

@Getter
@Builder
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

    public static Address asEntity(AddressDto dto) {
        return Address.builder()
                .id(dto.id)
                .streetName(dto.streetName)
                .buildingNumber(dto.buildingNumber)
                .apartmentNumber(dto.apartmentNumber)
                .zipCode(dto.zipCode)
                .cityName(dto.cityName)
                .voivodeship(dto.voivodeship)
                .build();
    }

    public static AddressDto asDto(Address entity) {
        return AddressDto.builder()
                .id(entity.getId())
                .streetName(entity.getStreetName())
                .buildingNumber(entity.getBuildingNumber())
                .apartmentNumber(entity.getApartmentNumber())
                .zipCode(entity.getZipCode())
                .cityName(entity.getCityName())
                .voivodeship(entity.getVoivodeship())
                .build();
    }
}
