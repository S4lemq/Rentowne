package pl.rentowne.tenant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.model.dto.TenantDto;
import pl.rentowne.tenant.repository.TenantRepository;
import pl.rentowne.tenant.service.TenantService;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    @Transactional
    public Long addTenantAndLeaseAgreement(TenantDto tenantDto) {
        return tenantRepository.save(TenantDto.asEntity(tenantDto)).getId();
    }

    @Override
    public TenantDto getTenantAndLeaseAgreement(Long id) throws RentowneNotFoundException {
        Tenant tenantEntity = tenantRepository.findByTenantId(id).orElseThrow(() -> new RentowneNotFoundException(id));
        return TenantDto.asDto(tenantEntity);
    }

    @Override
    @Transactional
    public void updateTenantAndLeaseAgreement(TenantDto dto) {
        tenantRepository.save(TenantDto.asEntity(dto));
    }
}
