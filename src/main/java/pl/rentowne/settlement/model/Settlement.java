package pl.rentowne.settlement.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.rented_object.model.RentedObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "SETTLEMENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_SETTLEMENT", columnNames = "SETTLEMENT_ID")
        },
        indexes = {
                @Index(name = "PK_SETTLEMENT", columnList = "SETTLEMENT_ID", unique = true)
        }
)
public class Settlement extends BaseEntity {
    @Id
    @SequenceGenerator(name = "settlement_seq", sequenceName = "SETTLEMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "settlement_seq")
    @Column(name = "SETTLEMENT_ID", nullable = false)
    private Long id;

    @Column(name = "ELECTRICITY_AMOUNT")
    private BigDecimal electricityAmount;

    @Column(name = "WATER_AMOUNT")
    private BigDecimal waterAmount;

    @Column(name = "GAS_AMOUNT")
    private BigDecimal gasAmount;

    @Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

    @Column(name = "SETTLEMENT_DATE")
    private LocalDateTime settlementDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RENTED_OBJECT_ID")
    private RentedObject rentedObject;

}
