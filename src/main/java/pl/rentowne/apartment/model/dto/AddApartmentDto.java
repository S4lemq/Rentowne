package pl.rentowne.apartment.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.rentowne.address.model.dto.AddressDto;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class AddApartmentDto {
    private String apartmentName;
    private BigInteger leasesNumber;
    private boolean isRented;
    private BigDecimal area;
    private AddressDto addressDto;
    private List<RentedObjectDto> rentedObjectDtos;

    @QueryProjection
    public AddApartmentDto(String apartmentName, BigInteger leasesNumber, boolean isRented, BigDecimal area,
                           AddressDto addressDto, List<RentedObjectDto> rentedObjectDtos) {
        this.apartmentName = apartmentName;
        this.leasesNumber = leasesNumber;
        this.isRented = isRented;
        this.area = area;
        this.addressDto = addressDto;
        this.rentedObjectDtos = rentedObjectDtos;
    }
}
