package pl.rentowne.tenant_settlement.model;

public enum TenantSettlementStatus {
    NEW("Nowe"),
    PAID("Opłacone");

    private String value;

    TenantSettlementStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
