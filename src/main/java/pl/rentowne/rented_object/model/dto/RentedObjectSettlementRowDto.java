package pl.rentowne.rented_object.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class RentedObjectSettlementRowDto implements DTRow {
    private Long rentedObjectId;
    private String apartment;
    private String rentedObject;
    private String tenantName;
    private String tenantSurname;
    private LocalDateTime lastSettlementDate;
    private BigDecimal lastSettlementTotalAmount;

    @QueryProjection
    public RentedObjectSettlementRowDto(Long rentedObjectId, String apartment, String rentedObject, String tenantName, String tenantSurname, LocalDateTime lastSettlementDate, BigDecimal lastSettlementTotalAmount) {
        this.rentedObjectId = rentedObjectId;
        this.apartment = apartment;
        this.rentedObject = rentedObject;
        this.tenantName = tenantName;
        this.tenantSurname = tenantSurname;
        this.lastSettlementDate = lastSettlementDate;
        this.lastSettlementTotalAmount = lastSettlementTotalAmount;
    }
}