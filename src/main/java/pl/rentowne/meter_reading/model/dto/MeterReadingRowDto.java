package pl.rentowne.meter_reading.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MeterReadingRowDto implements DTRow {
    private Long id;
    private BigDecimal currentReading;
    private LocalDateTime readingDate;
    private BigDecimal consumption;

    @QueryProjection
    public MeterReadingRowDto(Long id, BigDecimal currentReading, LocalDateTime readingDate, BigDecimal consumption) {
        this.id = id;
        this.currentReading = currentReading;
        this.readingDate = readingDate;
        this.consumption = consumption;
    }
}
