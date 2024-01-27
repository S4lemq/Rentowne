package pl.rentowne.leaseAgreement.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class LeaseAgreementDto {
    private Long id;
    private LocalDateTime startContractDate; //od kiedy najem
    private LocalDateTime endContractDate; //do kiedy najem
    private BigDecimal deposit; //kaucja
    private BigDecimal depositPaid; //wpłacona kaucja
    private int paymentDueDayOfMonth; //termin płatności
    private BigDecimal rentAmount; //kwota czynszu
    private BigDecimal compensationAmount; //kwota odstępnego
    private BigDecimal internetFee; //kwota za internet
    private BigDecimal gasDeposit; //opłata za gaz
    private BigDecimal includedWaterMeters; //ilość m^3 wliczonej wody
    private BigDecimal initialEnergyMeterReading; //początkowy odczyt energii
    private BigDecimal initialWaterMeterReading; //początkowy odczyt wody
    private BigDecimal initialGasMeterReading; //początkowy odczyt gazu
    private LocalDateTime depositReturnDate; //data zwrotu kaucji
    private BigDecimal returnedDepositAmount; //kwota zwróconej kaucji
    private boolean contractActive; //czy aktywna umowa

    public static LeaseAgreement asEntity(LeaseAgreementDto dto) {
        return LeaseAgreement.builder()
                .id(dto.id)
                .startContractDate(dto.startContractDate)
                .endContractDate(dto.endContractDate)
                .deposit(dto.deposit)
                .depositPaid(dto.depositPaid)
                .paymentDueDayOfMonth(dto.paymentDueDayOfMonth)
                .rentAmount(dto.rentAmount)
                .compensationAmount(dto.compensationAmount)
                .internetFee(dto.internetFee)
                .gasDeposit(dto.gasDeposit)
                .includedWaterMeters(dto.includedWaterMeters)
                .initialEnergyMeterReading(dto.initialEnergyMeterReading)
                .initialWaterMeterReading(dto.initialWaterMeterReading)
                .initialGasMeterReading(dto.initialGasMeterReading)
                .depositReturnDate(dto.depositReturnDate)
                .returnedDepositAmount(dto.returnedDepositAmount)
                .isContractActive(dto.contractActive)
                .build();
    }

    public static LeaseAgreementDto asDto(LeaseAgreement entity) {
        return LeaseAgreementDto.builder()
                .id(entity.getId())
                .startContractDate(entity.getStartContractDate())
                .endContractDate(entity.getEndContractDate())
                .deposit(entity.getDeposit())
                .depositPaid(entity.getDepositPaid())
                .paymentDueDayOfMonth(entity.getPaymentDueDayOfMonth())
                .rentAmount(entity.getRentAmount())
                .compensationAmount(entity.getCompensationAmount())
                .internetFee(entity.getInternetFee())
                .gasDeposit(entity.getGasDeposit())
                .includedWaterMeters(entity.getIncludedWaterMeters())
                .initialEnergyMeterReading(entity.getInitialEnergyMeterReading())
                .initialWaterMeterReading(entity.getInitialWaterMeterReading())
                .initialGasMeterReading(entity.getInitialGasMeterReading())
                .depositReturnDate(entity.getDepositReturnDate())
                .returnedDepositAmount(entity.getReturnedDepositAmount())
                .contractActive(entity.isContractActive())
                .build();
    }

}
