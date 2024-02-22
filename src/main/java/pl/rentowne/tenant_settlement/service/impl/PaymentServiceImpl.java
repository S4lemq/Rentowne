package pl.rentowne.tenant_settlement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.tenant_settlement.model.Payment;
import pl.rentowne.tenant_settlement.repository.PaymentRepository;
import pl.rentowne.tenant_settlement.service.PaymentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }
}
