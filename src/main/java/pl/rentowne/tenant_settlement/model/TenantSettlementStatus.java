package pl.rentowne.tenant_settlement.model;

public enum TenantSettlementStatus {
    NEW("Nowe"),
    PAID("Opłacone"),
    PROCESSING("Przetwarzane"),
    WAITING_FOR_DELIVERY("Czeka na dostawę"),
    COMPLETED("Zrealizowane"),
    CANCELED("Anulowane"),
    REFUND("Zwrócone");

    private String value;

    TenantSettlementStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
