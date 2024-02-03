package pl.rentowne.rented_object.model.dto;

import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;

@Getter
public class RentedObjectRowDto implements DTRow {
    private Long id;
    private String rentedObjectName;
    private boolean isRented;
    private String cityName;
    private String streetName;
    private String buildingNumber;
    private String apartmentNumber;
    private String image;
}
