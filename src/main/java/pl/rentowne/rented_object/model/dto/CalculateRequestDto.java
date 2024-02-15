package pl.rentowne.rented_object.model.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CalculateRequestDto {
    private boolean waterIncluded;
    private boolean electricityIncluded;
    private LocalDateTime settlementDate;
}
