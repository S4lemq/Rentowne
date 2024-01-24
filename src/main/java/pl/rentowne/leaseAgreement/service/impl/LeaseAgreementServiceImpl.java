package pl.rentowne.leaseAgreement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;
import pl.rentowne.leaseAgreement.model.dto.LeaseAgreementDto;
import pl.rentowne.leaseAgreement.repository.LeaseAgreementRepository;
import pl.rentowne.leaseAgreement.service.LeaseAgreementService;

@Service
@RequiredArgsConstructor
public class LeaseAgreementServiceImpl implements LeaseAgreementService {

    private final LeaseAgreementRepository leaseAgreementRepository;

    @Override
    @Transactional
    public Long addLease(LeaseAgreementDto leaseAgreementDto) {
        LeaseAgreement entity = LeaseAgreementDto.asEntity(leaseAgreementDto);
        return leaseAgreementRepository.save(entity).getId();
    }

    @Override
    public LeaseAgreementDto getDto(Long id) throws RentowneNotFoundException {
        LeaseAgreement leaseAgreement = leaseAgreementRepository.getLeaseById(id).orElseThrow(() -> new RentowneNotFoundException(id));
        return LeaseAgreementDto.asDto(leaseAgreement);
    }

    @Override
    public void updateLease(LeaseAgreementDto dto) {
        LeaseAgreement entity = LeaseAgreementDto.asEntity(dto);
        leaseAgreementRepository.save(entity);
    }
}
