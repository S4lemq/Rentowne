package pl.rentowne.tenant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;
import pl.rentowne.leaseAgreement.model.dto.LeaseAgreementDto;
import pl.rentowne.leaseAgreement.repository.LeaseAgreementRepository;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.model.dto.TenantDto;
import pl.rentowne.tenant.repository.TenantRepository;
import pl.rentowne.tenant.service.TenantService;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final LeaseAgreementRepository leaseAgreementRepository;

    @Override
    @Transactional
    public Long addTenant(TenantDto tenantDto) throws RentowneNotFoundException {
        Long leaseAgreementId = tenantDto.getLeaseAgreementDto().getId();
        LeaseAgreement leaseAgreementEntity = leaseAgreementRepository.getLeaseById(leaseAgreementId)
                .orElseThrow(() -> new RentowneNotFoundException(leaseAgreementId));

        return tenantRepository.save(TenantDto.asEntity(tenantDto, leaseAgreementEntity)).getId();
    }

    @Transactional
    @Override
    public TenantDto getTenant(Long id) throws RentowneNotFoundException {
        Tenant tenantEntity = tenantRepository.findByTenantId(id).orElseThrow(() -> new RentowneNotFoundException(id));
        return TenantDto.asDto(tenantEntity);
    }

}
