package pl.rentowne.tenant_settlement.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.tenant_settlement.model.Payment;
import pl.rentowne.tenant_settlement.model.TenantSettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class TenantSettlementSummary {
    private Long id;
    private LocalDateTime placeDate;
    private TenantSettlementStatus status;
    private BigDecimal grossValue;
    private Payment payment;
}
