package pl.rentowne.apartment.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;

import java.math.BigInteger;

@Getter
public class ApartmentRowDto implements DTRow {
    private Long id;
    private String apartmentName;
    private BigInteger leasesNumber;
    private boolean isRented;
    private String cityName;
    private String streetName;
    private String buildingNumber;
    private String apartmentNumber;
    private String image;

    @QueryProjection
    public ApartmentRowDto(Long id, String apartmentName, BigInteger leasesNumber, boolean isRented, String cityName,
                           String streetName, String buildingNumber, String apartmentNumber, String image) {
        this.id = id;
        this.apartmentName = apartmentName;
        this.leasesNumber = leasesNumber;
        this.isRented = isRented;
        this.cityName = cityName;
        this.streetName = streetName;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.image = image;
    }
}
