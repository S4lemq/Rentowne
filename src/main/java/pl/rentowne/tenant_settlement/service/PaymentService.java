package pl.rentowne.tenant_settlement.service;

import pl.rentowne.tenant_settlement.model.Payment;

import java.util.List;

public interface PaymentService {
    /**
     * Pobiera wszystkie typy płatności
     * @return lista {@ Payment}
     */
    List<Payment> getPayments();
}
