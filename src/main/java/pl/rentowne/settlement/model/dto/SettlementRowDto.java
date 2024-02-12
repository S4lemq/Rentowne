package pl.rentowne.settlement.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;
import pl.rentowne.util.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class SettlementRowDto implements DTRow {

    private String date;
    private BigDecimal compensationAmount; // kwota odstępnego
    private BigDecimal rentAmount; // kwota czynszu
    private BigDecimal internetFee; // kwota za internet
    private BigDecimal gasDeposit; // opłata za gaz
    private BigDecimal electricityAmount; // kwota elektryczna z liczników
    private BigDecimal waterAmount; // kwota wody z liczników
    private BigDecimal totalAmount; // kwota całkowita

    @QueryProjection
    public SettlementRowDto(LocalDateTime date, BigDecimal compensationAmount, BigDecimal rentAmount, BigDecimal internetFee, BigDecimal gasDeposit, BigDecimal electricityAmount, BigDecimal waterAmount, BigDecimal totalAmount) {
        this.date = DateUtils.formatPolishDate(date);
        this.compensationAmount = compensationAmount;
        this.rentAmount = rentAmount;
        this.internetFee = internetFee;
        this.gasDeposit = gasDeposit;
        this.electricityAmount = electricityAmount;
        this.waterAmount = waterAmount;
        this.totalAmount = totalAmount;
    }
}
