package pl.rentowne.tenant.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;
import pl.rentowne.settlement.model.SettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class TenantSettlementRowDto implements DTRow {
    private Long id;
    private LocalDateTime settlementDate;
    private BigDecimal totalAmount;
    private SettlementStatus settlementStatus;

    @QueryProjection
    public TenantSettlementRowDto(Long id, LocalDateTime settlementDate, BigDecimal totalAmount, SettlementStatus settlementStatus) {
        this.id = id;
        this.settlementDate = settlementDate;
        this.totalAmount = totalAmount;
        this.settlementStatus = settlementStatus;
    }
}
