package pl.rentowne.address.model.dto;

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
