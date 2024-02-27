package pl.rentowne.tenant_settlement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NotificationReceiveDto {
    private Integer merchantId;
    private Integer posId;
    private String sessionId;
    private Integer amount;
    private Integer originAmount;
    private String currency;
    private Long orderId;
    private Integer methodId;
    private String statement;
    private String sign;
}
