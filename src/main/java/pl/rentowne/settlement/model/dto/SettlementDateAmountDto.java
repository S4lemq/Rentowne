package pl.rentowne.settlement.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class SettlementDateAmountDto {
    private Long rentedObjectId;
    private LocalDateTime date;
    private BigDecimal totalAmount;

    @QueryProjection
    public SettlementDateAmountDto(Long rentedObjectId, LocalDateTime date, BigDecimal totalAmount) {
        this.rentedObjectId = rentedObjectId;
        this.date = date;
        this.totalAmount = totalAmount;
    }
}
