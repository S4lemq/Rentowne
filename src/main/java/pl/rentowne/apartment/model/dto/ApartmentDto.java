package pl.rentowne.apartment.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.rentowne.address.model.dto.AddressDto;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.rented_object.model.dto.RentedObjectDto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentDto {
    private Long id;
    private String apartmentName;
    private BigInteger leasesNumber;
    private boolean isRented;
    private BigDecimal area;
    private String image;
    private boolean pinned;
    private AddressDto addressDto;
    private List<RentedObjectDto> rentedObjectDtos;

    public static ApartmentDto asDto(Apartment apartment){
        return ApartmentDto.builder()
                .id(apartment.getId())
                .apartmentName(apartment.getApartmentName())
                .leasesNumber(apartment.getLeasesNumber())
                .isRented(apartment.isRented())
                .area(apartment.getArea())
                .pinned(apartment.isPinned())
                .image(apartment.getImage())
                .addressDto(AddressDto.asDto(apartment.getAddress()))
                .rentedObjectDtos(
                        apartment.getRentedObjects().stream().map(RentedObjectDto::asDtoWithoutMeters).toList()
                )
                .build();
    }

    public static ApartmentDto asBasicDto(Apartment apartment) {
        return ApartmentDto.builder()
                .id(apartment.getId())
                .apartmentName(apartment.getApartmentName())
                .build();
    }

    @QueryProjection
    public ApartmentDto(Long id, String apartmentName) {
        this.id = id;
        this.apartmentName = apartmentName;
    }
}
