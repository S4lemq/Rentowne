package pl.rentowne.lease_agreement.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class LeaseAgreementRowDto implements DTRow {
    private Long tenantId;
    private String apartment;
    private String rentedObject;
    private BigDecimal compensationAmount; //kwota odstępnego
    private BigDecimal rentAmount; //kwota czynszu
    private BigDecimal internetFee; //kwota za internet
    private BigDecimal gasDeposit; //opłata za gaz
    private BigDecimal includedWaterMeters; //ilość m^3 wliczonej wody
    private LocalDateTime startContractDate; //od kiedy najem
    private LocalDateTime endContractDate; //do kiedy najem
    private BigDecimal deposit; //kaucja

    @QueryProjection
    public LeaseAgreementRowDto(Long tenantId, String apartment, String rentedObject, BigDecimal compensationAmount, BigDecimal rentAmount, BigDecimal internetFee, BigDecimal gasDeposit, BigDecimal includedWaterMeters, LocalDateTime startContractDate, LocalDateTime endContractDate, BigDecimal deposit) {
        this.tenantId = tenantId;
        this.apartment = apartment;
        this.rentedObject = rentedObject;
        this.compensationAmount = compensationAmount;
        this.rentAmount = rentAmount;
        this.internetFee = internetFee;
        this.gasDeposit = gasDeposit;
        this.includedWaterMeters = includedWaterMeters;
        this.startContractDate = startContractDate;
        this.endContractDate = endContractDate;
        this.deposit = deposit;
    }
}
