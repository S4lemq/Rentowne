package pl.rentowne.tenant_settlement.service.payment.p24;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TransactionVerifyRequest {
    private Integer merchantId;
    private Integer posId;
    private String sessionId;
    private Integer amount;
    private String currency;
    private Long orderId;
    private String sign;
}
