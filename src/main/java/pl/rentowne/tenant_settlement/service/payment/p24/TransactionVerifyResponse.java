package pl.rentowne.tenant_settlement.service.payment.p24;

import lombok.Getter;

@Getter
public class TransactionVerifyResponse {
    private Data data;

    record Data (String status) {}
}
