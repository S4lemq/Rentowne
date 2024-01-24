package pl.rentowne.rentedObject.model.dto;

import lombok.Getter;
import pl.rentowne.dataTable.dtDefinition.DTRow;

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
