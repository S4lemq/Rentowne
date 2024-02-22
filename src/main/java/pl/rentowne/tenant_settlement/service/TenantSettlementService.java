package pl.rentowne.tenant_settlement.service;

import pl.rentowne.tenant_settlement.model.dto.TenantSettlementDto;
import pl.rentowne.tenant_settlement.model.dto.TenantSettlementSummary;

public interface TenantSettlementService {
    TenantSettlementSummary placeSettlement(TenantSettlementDto dto);
}
