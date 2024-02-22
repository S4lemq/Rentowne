package pl.rentowne.tenant_settlement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.repository.TenantRepository;
import pl.rentowne.tenant_settlement.model.Payment;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import pl.rentowne.tenant_settlement.model.TenantSettlementStatus;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementDto;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementSummary;
import pl.rentowne.tenant_settlement.repository.PaymentRepository;
import pl.rentowne.tenant_settlement.repository.TenantSettlementRepository;
import pl.rentowne.tenant_settlement.service.TenantSettlementService;
import pl.rentowne.user.service.UserService;

@Service
@RequiredArgsConstructor
public class TenantSettlementServiceImpl implements TenantSettlementService {

    private final TenantSettlementRepository tenantSettlementRepository;
    private final TenantRepository tenantRepository;
    private final UserService userService;
    private final PaymentRepository paymentRepository;

    @Override
    public TenantSettlementSummary placeSettlement(TenantSettlementDto dto) {
        String loggedEmail = userService.getLoggedUser().getEmail();
        Long tenantId = tenantRepository.getTenantIdByEmail(loggedEmail);
        Payment payment = paymentRepository.findById(dto.getPaymentId()).orElseThrow(() -> new RentowneNotFoundException(dto.getPaymentId()));

        TenantSettlement toSave = TenantSettlement.builder()
                .tenantSettlementStatus(TenantSettlementStatus.NEW)
                .grossValue(dto.getTotalAmount())
                .tenant(Tenant.builder().id(tenantId).build())
                .payment(payment)
                .build();

        TenantSettlement savedSettlement = tenantSettlementRepository.save(toSave);

        return TenantSettlementSummary.builder()
                .id(savedSettlement.getId())
                .placeDate(savedSettlement.getInsertDate())
                .status(savedSettlement.getTenantSettlementStatus())
                .grossValue(savedSettlement.getGrossValue())
                .payment(savedSettlement.getPayment())
                .build();
    }
}
