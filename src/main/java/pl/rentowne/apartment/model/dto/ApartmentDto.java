package pl.rentowne.apartment.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.rentowne.address.model.dto.AddressDto;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

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
    private AddressDto addressDto;
    private Set<RentedObjectDto> rentedObjectDtos;

    @QueryProjection
    public ApartmentDto(Long id, String apartmentName, BigInteger leasesNumber, boolean isRented, BigDecimal area, AddressDto addressDto) {
        this.id = id;
        this.apartmentName = apartmentName;
        this.leasesNumber = leasesNumber;
        this.isRented = isRented;
        this.area = area;
        this.addressDto = addressDto;
    }
}
