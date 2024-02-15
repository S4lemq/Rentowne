package pl.rentowne.meter.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;
import pl.rentowne.meter.model.MeterType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MeterRowDto implements DTRow {
    private Long id;
    private String apartment;
    private String rentedObject;
    private String meterName;
    private MeterType meterType;
    private String meterNumber;
    private BigDecimal initialMeterReading;
    private LocalDateTime installationDate;

    @QueryProjection
    public MeterRowDto(Long id, String apartment, String rentedObject, String meterName, MeterType meterType, String meterNumber, BigDecimal initialMeterReading, LocalDateTime installationDate) {
        this.id = id;
        this.apartment = apartment;
        this.rentedObject = rentedObject;
        this.meterName = meterName;
        this.meterType = meterType;
        this.meterNumber = meterNumber;
        this.initialMeterReading = initialMeterReading;
        this.installationDate = installationDate;
    }
}
