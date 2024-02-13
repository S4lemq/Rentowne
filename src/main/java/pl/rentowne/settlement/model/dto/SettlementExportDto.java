package pl.rentowne.settlement.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class SettlementExportDto {

    private LocalDateTime date; // data rozliczenia
    private BigDecimal compensationAmount; // kwota odstępnego
    private BigDecimal rentAmount; // kwota czynszu
    private BigDecimal internetFee; // kwota za internet
    private BigDecimal gasDeposit; // opłata za gaz
    private BigDecimal electricityAmount; // kwota elektryczna z liczników
    private BigDecimal waterAmount; // kwota wody z liczników
    private BigDecimal totalAmount; // kwota całkowita

    @QueryProjection
    public SettlementExportDto(LocalDateTime date, BigDecimal compensationAmount, BigDecimal rentAmount, BigDecimal internetFee, BigDecimal gasDeposit, BigDecimal electricityAmount, BigDecimal waterAmount, BigDecimal totalAmount) {
        this.date = date;
        this.compensationAmount = compensationAmount;
        this.rentAmount = rentAmount;
        this.internetFee = internetFee;
        this.gasDeposit = gasDeposit;
        this.electricityAmount = electricityAmount;
        this.waterAmount = waterAmount;
        this.totalAmount = totalAmount;
    }
}
