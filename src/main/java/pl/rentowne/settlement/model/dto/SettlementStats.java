package pl.rentowne.settlement.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class SettlementStats {
    private List<String> label;
    private List<BigDecimal> totalAmount;
    private List<Long> tenant;
}
