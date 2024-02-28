package pl.rentowne.tenant_settlement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.repository.TenantRepository;
import pl.rentowne.tenant_settlement.model.*;
import pl.rentowne.tenant_settlement.model.dto.NotificationReceiveDto;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementDto;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementSummary;
import pl.rentowne.tenant_settlement.repository.PaymentRepository;
import pl.rentowne.tenant_settlement.repository.TenantSettlementLogRepository;
import pl.rentowne.tenant_settlement.repository.TenantSettlementRepository;
import pl.rentowne.tenant_settlement.service.TenantSettlementService;
import pl.rentowne.tenant_settlement.service.payment.p24.PaymentMethodP24;
import pl.rentowne.user.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TenantSettlementServiceImpl implements TenantSettlementService {

    private final TenantSettlementRepository tenantSettlementRepository;
    private final TenantRepository tenantRepository;
    private final UserService userService;
    private final PaymentRepository paymentRepository;
    private final PaymentMethodP24 paymentMethodP24;
    private final TenantSettlementLogRepository tenantSettlementLogRepository;

    @Override
    @Transactional
    public TenantSettlementSummary placeSettlement(TenantSettlementDto dto) {
        String loggedEmail = userService.getLoggedUser().getEmail();
        Tenant tenant = tenantRepository.getTenantByEmail(loggedEmail);
        Payment payment = paymentRepository.findById(dto.getPaymentId()).orElseThrow(() -> new RentowneNotFoundException(dto.getPaymentId()));

        TenantSettlement toSave = TenantSettlement.builder()
                .tenantSettlementStatus(TenantSettlementStatus.NEW)
                .grossValue(dto.getTotalAmount())
                .tenant(Tenant.builder().id(tenant.getId()).build())
                .payment(payment)
                .orderHash(RandomStringUtils.randomAlphanumeric(12))
                .build();

        TenantSettlement savedSettlement = tenantSettlementRepository.save(toSave);
        savedSettlement.setTenant(tenant);
        String redirectUrl = initPaymentIfNeeded(savedSettlement);

        return createTenantSettlementSummary(savedSettlement, redirectUrl);
    }

    @Override
    public TenantSettlement getTenantSettlementByOrderHash(String orderHash) {
        return tenantSettlementRepository.findByOrderHash(orderHash).orElseThrow();
    }

    @Override
    @Transactional
    public void receiveNotification(String orderHash, NotificationReceiveDto receiveDto, String remoteAddr) {
        TenantSettlement tenantSettlement = getTenantSettlementByOrderHash(orderHash);
        String status = paymentMethodP24.receiveNotification(tenantSettlement, receiveDto, remoteAddr);
        if (status.equals("success")) {
            TenantSettlementStatus oldStatus = tenantSettlement.getTenantSettlementStatus();
            tenantSettlement.setTenantSettlementStatus(TenantSettlementStatus.PAID);
            tenantSettlementLogRepository.save(TenantSettlementLog.builder()
                            .created(LocalDateTime.now())
                            .tenantSettlementId(tenantSettlement.getId())
                            .note("Opłacono płatność wynajmu przez Przelewy24, id płatności: " + receiveDto.getStatement() +
                                    ", zmieniono status z " + oldStatus.getValue() + " na " + tenantSettlement.getTenantSettlementStatus().getValue())
                    .build());
        }
    }

    private static TenantSettlementSummary createTenantSettlementSummary(TenantSettlement savedSettlement, String redirectUrl) {
        return TenantSettlementSummary.builder()
                .id(savedSettlement.getId())
                .placeDate(savedSettlement.getInsertDate())
                .status(savedSettlement.getTenantSettlementStatus())
                .grossValue(savedSettlement.getGrossValue())
                .payment(savedSettlement.getPayment())
                .redirectUrl(redirectUrl)
                .build();
    }

    private String initPaymentIfNeeded(TenantSettlement tenantSettlement) {
        if (tenantSettlement.getPayment().getType() == PaymentType.P24_ONLINE) {
            return paymentMethodP24.initPayment(tenantSettlement);
        }
        return null;
    }
}
