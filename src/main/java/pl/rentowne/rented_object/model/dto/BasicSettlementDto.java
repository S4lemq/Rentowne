package pl.rentowne.rented_object.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import pl.rentowne.tenant_settlement.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BasicSettlementDto {
    private BigDecimal totalAmount;
    private LocalDateTime settlementDate;
    private List<Payment> payment;

    @QueryProjection
    public BasicSettlementDto(BigDecimal totalAmount, LocalDateTime settlementDate) {
        this.totalAmount = totalAmount;
        this.settlementDate = settlementDate;
    }
}
