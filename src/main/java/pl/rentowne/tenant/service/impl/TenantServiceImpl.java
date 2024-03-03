package pl.rentowne.tenant.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.apartment.repository.ApartmentRepository;
import pl.rentowne.apartment.service.ApartmentService;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.meter.model.Meter;
import pl.rentowne.meter.model.MeterType;
import pl.rentowne.meter_reading.model.MeterReading;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;
import pl.rentowne.meter_reading.repository.MeterReadingRepository;
import pl.rentowne.rented_object.repository.RentedObjectRepository;
import pl.rentowne.security.service.AuthenticationService;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.model.dto.TenantDto;
import pl.rentowne.tenant.repository.TenantRepository;
import pl.rentowne.tenant.service.TenantService;
import pl.rentowne.util.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final ApartmentService apartmentService;
    private final RentedObjectRepository rentedObjectRepository;
    private final ApartmentRepository apartmentRepository;
    private final AuthenticationService authenticationService;
    private final MeterReadingRepository meterReadingRepository;

    @Override
    @Transactional
    public Long addTenantAndLeaseAgreement(TenantDto tenantDto) throws RentowneBusinessException {
        Long rentedObjectId = tenantDto.getRentedObjectDto().getId();
        apartmentService.updateRentedFlagOnApartment(rentedObjectId);
        Long tenantId = tenantRepository.save(TenantDto.asEntity(tenantDto)).getId();
        LocalDateTime currentReadingDate = tenantDto.getLeaseAgreementDto().getStartContractDate();

        processMeterReading(tenantDto.getLeaseAgreementDto().getInitialWaterMeterReading(), MeterType.WATER_COLD, rentedObjectId, currentReadingDate);
        processMeterReading(tenantDto.getLeaseAgreementDto().getInitialEnergyMeterReading(), MeterType.ELECTRIC, rentedObjectId, currentReadingDate);
        processMeterReading(tenantDto.getLeaseAgreementDto().getInitialGasMeterReading(), MeterType.GAS, rentedObjectId, currentReadingDate);

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

    private void processMeterReading(BigDecimal initialReading, MeterType meterType, Long rentedObjectId, LocalDateTime currentReadingDate) {
        if (initialReading != null) {
            MeterReadingDto meterIdAndConsumption = meterReadingRepository.getMeterIdReadingValueAndDate(rentedObjectId, meterType);

            Optional.ofNullable(meterIdAndConsumption.getCurrentReading())
                    .ifPresent(currentReading -> {
                        if (initialReading.compareTo(currentReading) < 0) {
                            throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_VALUES_TENANT);
                        }
                        DateUtils.compareDates(currentReadingDate, meterIdAndConsumption.getReadingDate(), true);
                    });

            BigDecimal consumption = Optional.ofNullable(meterIdAndConsumption.getCurrentReading())
                    .map(initialReading::subtract)
                    .orElse(BigDecimal.ZERO);

            MeterReading meterReading = MeterReading.builder()
                    .currentReading(initialReading)
                    .readingDate(currentReadingDate)
                    .consumption(consumption)
                    .meter(Meter.builder().id(meterIdAndConsumption.getMeterDto().getId()).build())
                    .settled(true)
                    .build();
            meterReadingRepository.save(meterReading);
        }
    }

    private void secondUpdateFlags(TenantDto dto) {
        Long rentedObjectId = dto.getRentedObjectDto().getId();
        rentedObjectRepository.updateRentedFlag(rentedObjectId, true);
        Long apartmentId = dto.getApartment().getId();
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
