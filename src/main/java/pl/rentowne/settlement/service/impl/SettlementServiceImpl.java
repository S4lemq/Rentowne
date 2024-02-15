package pl.rentowne.settlement.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.model.ProviderType;
import pl.rentowne.housing_provider.repository.HousingProviderRepository;
import pl.rentowne.lease_agreement.model.dto.LeaseAgreementDto;
import pl.rentowne.lease_agreement.repository.LeaseAgreementRepository;
import pl.rentowne.meter.model.MeterType;
import pl.rentowne.meter.repository.MeterRepository;
import pl.rentowne.meter_reading.model.dto.MeterReadingDto;
import pl.rentowne.meter_reading.repository.MeterReadingRepository;
import pl.rentowne.provider_field.model.BillingMethod;
import pl.rentowne.provider_field.model.ProviderField;
import pl.rentowne.rented_object.model.RentedObject;
import pl.rentowne.rented_object.model.dto.CalculateRequestDto;
import pl.rentowne.rented_object.repository.RentedObjectRepository;
import pl.rentowne.settlement.model.Settlement;
import pl.rentowne.settlement.repository.SettlementRepository;
import pl.rentowne.settlement.service.SettlementService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SettlementServiceImpl implements SettlementService {
    private final SettlementRepository settlementRepository;
    private final HousingProviderRepository housingProviderRepository;
    private final MeterRepository meterRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final LeaseAgreementRepository leaseAgreementRepository;
    private final RentedObjectRepository rentedObjectRepository;

    @Transactional
    @Override
    public void calculate(Long rentedObjectId, CalculateRequestDto dto) {
        List<HousingProvider> providerWithProviderFields = housingProviderRepository.findHousingProviderWithProviderFieldsByRentedObjectId(rentedObjectId);
        LeaseAgreementDto leaseAgreement = leaseAgreementRepository.getData(rentedObjectId);
        List<Long> allMeterReadingIds = new ArrayList<>();
        BigDecimal electricResult = BigDecimal.ZERO;
        BigDecimal gasResult = BigDecimal.ZERO;
        BigDecimal waterResult = BigDecimal.ZERO;


        if (dto.isElectricityIncluded()) {
            electricResult = this.calculateElectricityCost(providerWithProviderFields, rentedObjectId, electricResult, allMeterReadingIds);
        }
        if (dto.isWaterIncluded()) {
            waterResult = this.calculateWaterCost(providerWithProviderFields, rentedObjectId, waterResult, allMeterReadingIds, leaseAgreement);
        }
        gasResult = leaseAgreement.getGasDeposit() != null ? leaseAgreement.getGasDeposit() : BigDecimal.ZERO;
//        gasResult = this.calculateGasCost(providerWithProviderFields, rentedObjectId, gasResult, allMeterReadingIds);
//        administrativeResult = this.calculateAdministrativeFee(providerWithProviderFields);

        if (!allMeterReadingIds.isEmpty()) {
            meterReadingRepository.updateSettled(allMeterReadingIds);
        }

        BigDecimal totalSum = electricResult.add(waterResult).add(gasResult).add(leaseAgreement.getCompensationAmount()).add(leaseAgreement.getInternetFee()).add(leaseAgreement.getRentAmount());

        log.info("Rozliczenie obiektu o id: {}", rentedObjectId);
        log.info("Rozliczano: wodę-{}, prąd-{}", dto.isWaterIncluded(), dto.isElectricityIncluded());
        log.info("Kwota za prąd: {}", electricResult);
        log.info("Kwota za wodę: {}", waterResult);
        log.info("Kwota za gaz: {}", gasResult);
        log.info("Kwota odstępnego: {}", leaseAgreement.getCompensationAmount());
        log.info("Kwota za internet: {}", leaseAgreement.getInternetFee());
        log.info("Kwota czynszu: {}", leaseAgreement.getRentAmount());
        log.info("Kwota całkowita: {}", totalSum);
        log.info("W następujących odczytach liczników ustawiono odczyty na rozliczone: {}", allMeterReadingIds);

        Settlement settlement = Settlement.builder()
                .electricityAmount(electricResult)
                .waterAmount(waterResult)
                .gasAmount(gasResult)
                .totalAmount(totalSum)
                .settlementDate(dto.getSettlementDate())
                .rentedObject(RentedObject.builder().id(rentedObjectId).build())
                .build();

        settlementRepository.save(settlement);
        rentedObjectRepository.updateSettlement(rentedObjectId, totalSum, dto.getSettlementDate());
    }


    // Nowa metoda dla wyszukiwania dostawcy
    private Optional<HousingProvider> findProvider(List<HousingProvider> providers, ProviderType type) {
        return providers.stream()
                .filter(dto -> type.equals(dto.getType()))
                .findFirst();
    }

    private BigDecimal calculateForProviderField(ProviderField field, BigDecimal consumption) {
        BigDecimal result = BigDecimal.ZERO;
        if (BillingMethod.MONTHLY.equals(field.getBillingMethod())) {
            result = result.add(this.calculateByMonth(field.getPrice()));
        } else if (BillingMethod.ACCORDING_TO_CONSUMPTION.equals(field.getBillingMethod())) {
            result = result.add(this.calculateByConsumption(field.getPrice(), consumption));
        }
        return result;
    }

    public BigDecimal calculateElectricityCost(List<HousingProvider> providerWithProviderFields, Long rentedObjectId,  BigDecimal electricResult, List<Long> allMeterReadingIds) {
        Optional<HousingProvider> electricProviderOptional = findProvider(providerWithProviderFields, ProviderType.ELECTRICITY);

        if (electricProviderOptional.isPresent()) {
            HousingProvider electricProvider = electricProviderOptional.get();
            List<MeterReadingDto> meterReadingDtos = meterRepository.getConsumptionAndIdByRentedObjectAndMeterType(rentedObjectId, MeterType.ELECTRIC);
            BigDecimal consumption = meterReadingDtos.stream()
                    .map(MeterReadingDto::getConsumption)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            List<Long> meterReadingIds = meterReadingDtos.stream()
                    .map(MeterReadingDto::getId)
                    .toList();
            allMeterReadingIds.addAll(meterReadingIds);

            // Używamy strumienia do przetworzenia providerFields, obliczania i sumowania wyników
            BigDecimal totalElectricResult = electricProvider.getProviderFields().stream()
                    .map(field -> calculateForProviderField(field, consumption))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            electricResult = electricResult.add(totalElectricResult);

            if (electricProvider.getTax() != null) {
                electricResult = this.calculateVat(electricProvider.getTax(), electricResult);
            }
            return electricResult;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateGasCost(List<HousingProvider> providerWithProviderFields, Long rentedObjectId, BigDecimal gasResult, List<Long> allMeterReadingIds) {
        Optional<HousingProvider> gasProviderOptional = findProvider(providerWithProviderFields, ProviderType.GAS);

        if (gasProviderOptional.isPresent()) {
            HousingProvider gasProvider = gasProviderOptional.get();
            BigDecimal conversionRate = Optional.ofNullable(gasProvider.getConversionRate()).orElse(BigDecimal.ONE);
            List<MeterReadingDto> meterReadingDtos = meterRepository.getConsumptionAndIdByRentedObjectAndMeterType(rentedObjectId, MeterType.GAS);

            BigDecimal consumption = meterReadingDtos.stream()
                    .map(MeterReadingDto::getConsumption)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            List<Long> meterReadingIds = meterReadingDtos.stream()
                    .map(MeterReadingDto::getId)
                    .toList();
            allMeterReadingIds.addAll(meterReadingIds);
            // Używamy strumienia do przetworzenia providerFields, obliczania i sumowania wyników
            BigDecimal totalGasResult = gasProvider.getProviderFields().stream()
                    .map(field -> calculateForGasProviderField(field, consumption, conversionRate))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            gasResult = gasResult.add(totalGasResult);

            if (gasProvider.getTax() != null) {
                gasResult = this.calculateVat(gasProvider.getTax(), gasResult);
            }
            return gasResult;
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal calculateForGasProviderField(ProviderField field, BigDecimal consumption, BigDecimal conversionRate) {
        BigDecimal result = BigDecimal.ZERO;
        if (BillingMethod.MONTHLY.equals(field.getBillingMethod())) {
            result = result.add(this.calculateByMonth(field.getPrice()));
        } else if (BillingMethod.ACCORDING_TO_CONSUMPTION.equals(field.getBillingMethod())) {
            BigDecimal resultWithDecimals = consumption.multiply(conversionRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal integerResult = new BigDecimal(resultWithDecimals.toBigInteger());
            result = result.add(integerResult.multiply(field.getPrice()).setScale(2, RoundingMode.HALF_UP));
        }
        return result;
    }

    public BigDecimal calculateWaterCost(List<HousingProvider> providerWithProviderFields, Long rentedObjectId, BigDecimal waterResult, List<Long> allMeterReadingIds, LeaseAgreementDto leaseAgreement) {
        Optional<HousingProvider> waterHousingProviderOptional = findProvider(providerWithProviderFields, ProviderType.WATER);

        if (waterHousingProviderOptional.isPresent()) {
            HousingProvider waterHousingProvider = waterHousingProviderOptional.get();
            Optional<ProviderField> optionalField = waterHousingProvider.getProviderFields().stream().findFirst();

            if (optionalField.isPresent()) {
                ProviderField field = optionalField.get();
                List<MeterReadingDto> meterReadingDtos = meterRepository.getConsumptionAndIdByRentedObjectAndMeterType(rentedObjectId, MeterType.WATER_COLD);
                BigDecimal consumption = meterReadingDtos.stream()
                        .map(MeterReadingDto::getConsumption)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                allMeterReadingIds.addAll(meterReadingDtos.stream()
                        .map(MeterReadingDto::getId)
                        .toList());

                // Bezpośrednie obliczenie kosztu wody na podstawie zużycia
                waterResult = waterResult.add(this.calculateByConsumption(field.getPrice(), consumption));

                // Aplikowanie podatku VAT, jeśli jest ustawiony
                if (waterHousingProvider.getTax() != null) {
                    waterResult = this.calculateVat(waterHousingProvider.getTax(), waterResult);
                }

                // Odjęcie wliczonych w cenę jednostek wody
                BigDecimal includedWaterMeters = this.calculateByConsumption(field.getPrice(), leaseAgreement.getIncludedWaterMeters());
                waterResult = waterResult.subtract(includedWaterMeters);
            }
            return waterResult;
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal calculateAdministrativeFee(List<HousingProvider> providerWithProviderFields) {
        return providerWithProviderFields.stream()
                .filter(dto -> ProviderType.ADMINISTRATIVE_FEE.equals(dto.getType()))
                .findFirst()
                .map(administrativeProvider -> administrativeProvider.getProviderFields().stream()
                        .map(ProviderField::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateByMonth(final BigDecimal entityField) {
        return BigDecimal.ONE.multiply(entityField).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateByConsumption(final BigDecimal entityField, BigDecimal consumptionAfterConversion) {
        return consumptionAfterConversion.multiply(entityField).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateVat(final BigDecimal vat, BigDecimal netAmount) {
        return netAmount.multiply(vat.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP).add(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP);
    }

}
