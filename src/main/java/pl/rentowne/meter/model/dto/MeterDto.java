package pl.rentowne.meter.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.meter.model.MeterType;
import pl.rentowne.rented_object.model.dto.RentedObjectDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class MeterDto {
    private Long id;
    private String name;
    private MeterType meterType;
    private RentedObjectDto rentedObject;
    private String meterNumber;
    private BigDecimal initialMeterReading;
    private LocalDateTime installationDate;
}
