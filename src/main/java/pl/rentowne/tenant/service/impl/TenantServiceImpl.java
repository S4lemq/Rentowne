package pl.rentowne.tenant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.apartment.repository.ApartmentRepository;
import pl.rentowne.apartment.service.ApartmentService;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.rentedObject.repository.RentedObjectRepository;
import pl.rentowne.security.service.AuthenticationService;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.model.dto.TenantDto;
import pl.rentowne.tenant.repository.TenantRepository;
import pl.rentowne.tenant.service.TenantService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final ApartmentService apartmentService;
    private final RentedObjectRepository rentedObjectRepository;
    private final ApartmentRepository apartmentRepository;
    private final AuthenticationService authenticationService;

    @Override
    @Transactional
    public Long addTenantAndLeaseAgreement(TenantDto tenantDto) {
        apartmentService.updateRentedFlagOnApartment(tenantDto.getRentedObjectDto().getId());
        Long tenantId = tenantRepository.save(TenantDto.asEntity(tenantDto)).getId();
        authenticationService.createTenantAccount(tenantDto);
        return tenantId;
    }

    @Override
    public TenantDto getTenantAndLeaseAgreement(Long id) throws RentowneNotFoundException {
        Tenant tenantEntity = tenantRepository.findByTenantId(id).orElseThrow(() -> new RentowneNotFoundException(id));
        return TenantDto.asDto(tenantEntity);
    }

    @Override
    @Transactional
    public void updateTenantAndLeaseAgreement(TenantDto dto) {
        Long rentedObjectIdInDB = tenantRepository.getRentedObjectId(dto.getId());
        this.firstUpdateFlags(rentedObjectIdInDB);
        tenantRepository.save(TenantDto.asEntity(dto));
        this.secondUpdateFlags(dto);

    }

    private void secondUpdateFlags(TenantDto dto) {
        Long rentedObjectId = dto.getRentedObjectDto().getId();
        rentedObjectRepository.updateRentedFlag(rentedObjectId, true);
        Long apartmentId = dto.getApartmentId();
        List<Long> ids = apartmentRepository.getAllRentedObjectsByApartmentIdAndRentedFlag(apartmentId, false);
        if (ids.isEmpty()) {
            apartmentRepository.updateRentedFlag(apartmentId, true);
        }
    }

    private void firstUpdateFlags(Long rentedObjectIdInDB) {
        rentedObjectRepository.updateRentedFlag(rentedObjectIdInDB, false);
        Long apartmentId = apartmentRepository.getApartmentIdByRentedObject(rentedObjectIdInDB);
        apartmentRepository.updateRentedFlag(apartmentId, false);
    }
}
