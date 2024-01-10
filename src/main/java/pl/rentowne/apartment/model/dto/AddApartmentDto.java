package pl.rentowne.apartment.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.address.model.dto.AddressDto;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
public class AddApartmentDto {
    private String apartmentName;
    private BigInteger leasesNumber;
    private boolean isRented;
    private BigDecimal area;
    private AddressDto addressDto;

    @QueryProjection
    public AddApartmentDto(String apartmentName, BigInteger leasesNumber, boolean isRented, BigDecimal area,
                           AddressDto addressDto) {
        this.apartmentName = apartmentName;
        this.leasesNumber = leasesNumber;
        this.isRented = isRented;
        this.area = area;
        this.addressDto = addressDto;
    }
}
