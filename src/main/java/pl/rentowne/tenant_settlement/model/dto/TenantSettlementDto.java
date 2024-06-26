package pl.rentowne.tenant_settlement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantSettlementDto {
    private BigDecimal totalAmount;
    private Long paymentId;
}
