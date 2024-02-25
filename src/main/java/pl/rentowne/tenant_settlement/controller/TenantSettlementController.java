package pl.rentowne.tenant_settlement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.rented_object.model.dto.BasicSettlementDto;
import pl.rentowne.rented_object.service.RentedObjectService;
import pl.rentowne.tenant_settlement.model.dto.NotificationDto;
import pl.rentowne.tenant_settlement.model.dto.NotificationReceiveDto;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import pl.rentowne.tenant_settlement.model.TenantSettlementStatus;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementDto;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementSummary;
import pl.rentowne.tenant_settlement.service.PaymentService;
import pl.rentowne.tenant_settlement.service.TenantSettlementService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rozliczenia najemców")
@Validated
@Slf4j
public class TenantSettlementController extends AbstractController {

    private final RentedObjectService rentedObjectService;
    private final TenantSettlementService tenantSettlementService;
    private final PaymentService paymentService;

    @GetMapping(value = "/api/tenant/get-financial-data")
    public ResponseEntity<BasicSettlementDto> getBasicSettlementData() throws RentowneBusinessException {
        BasicSettlementDto basicSettlementData = rentedObjectService.getBasicSettlementData();
        basicSettlementData.setPayment(paymentService.getPayments());
        return ResponseEntity.ok(basicSettlementData);
    }

    @PostMapping(value = "/api/tenant/tenant-settlement")
    public TenantSettlementSummary placeSettlement(@RequestBody TenantSettlementDto dto) {
        return tenantSettlementService.placeSettlement(dto);
    }

    @GetMapping("/api/tenant/notification/{orderHash}") //dla frontendu, wyświetla czy opłacone
    public NotificationDto notificationShow(@PathVariable @Length(max = 12) String orderHash) {
        TenantSettlement tenantSettlement = tenantSettlementService.getTenantSettlementByOrderHash(orderHash);
        return new NotificationDto(tenantSettlement.getTenantSettlementStatus() == TenantSettlementStatus.PAID);
    }

    @PostMapping("/tenant/notification/{orderHash}") // odbiera z P24 notyfikacje
    public void notificationReceive(@PathVariable @Length(max = 12) String orderHash,
                                    @RequestBody NotificationReceiveDto receiveDto) {
        log.info("try receive notification");
        tenantSettlementService.receiveNotification(orderHash, receiveDto);
    }

}
