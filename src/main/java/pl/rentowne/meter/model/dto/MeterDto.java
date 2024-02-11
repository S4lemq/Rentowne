package pl.rentowne.meter.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.rentowne.meter.model.MeterType;
import pl.rentowne.rented_object.model.dto.RentedObjectDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeterDto {
    private Long id;
    private String name;
    private MeterType meterType;
    private RentedObjectDto rentedObject;
    private String meterNumber;
    private BigDecimal initialMeterReading;
    private LocalDateTime installationDate;

    @QueryProjection
    public MeterDto(Long id) {
        this.id = id;
    }
}
