package pl.rentowne.tenant_settlement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.rented_object.model.dto.BasicSettlementDto;
import pl.rentowne.rented_object.service.RentedObjectService;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementDto;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementSummary;
import pl.rentowne.tenant_settlement.service.PaymentService;
import pl.rentowne.tenant_settlement.service.TenantSettlementService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Rozliczenia najemców")
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

}
