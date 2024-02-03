package pl.rentowne.meter_reading.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.rentowne.meter.model.Meter;
import pl.rentowne.meter.model.dto.MeterDto;
import pl.rentowne.meter_reading.model.MeterReading;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterReadingDto {
    private Long id;
    private BigDecimal currentReading;
    private LocalDateTime readingDate;
    private BigDecimal consumption;
    private MeterDto meterDto;
    private BigDecimal previousReading;
    private LocalDateTime previousReadingDate;


    public static MeterReading asEntity(MeterReadingDto dto) {
        return MeterReading.builder()
                .id(dto.getId())
                .currentReading(dto.getCurrentReading())
                .readingDate(dto.getReadingDate())
                .consumption(
                        dto.getPreviousReading() != null ?
                        dto.getCurrentReading().subtract(dto.getPreviousReading()) :
                        BigDecimal.ZERO
                )
                .meter(Meter.builder()
                        .id(dto.getMeterDto().getId())
                        .build())
                .build();
    }
}
