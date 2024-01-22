package pl.rentowne.meter.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.meter.model.MeterType;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;

@Getter
@Builder
public class MeterDto {
    private Long id;
    private String name;
    private MeterType meterType;
    private RentedObjectDto rentedObject;
}
