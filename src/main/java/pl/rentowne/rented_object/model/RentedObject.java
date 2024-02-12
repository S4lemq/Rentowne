package pl.rentowne.rented_object.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.meter.model.Meter;
import pl.rentowne.settlement.model.Settlement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "RENTED_OBJECT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_RENTED_OBJECT", columnNames = "RENTED_OBJECT_ID")
        },
        indexes = {
                @Index(name = "PK_RENTED_OBJECT", columnList = "RENTED_OBJECT_ID", unique = true)
        }
)
public class RentedObject extends BaseEntity {

    @Id
    @SequenceGenerator(name = "rented_object_seq", sequenceName = "RENTED_OBJECT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rented_object_seq")
    @Column(name = "RENTED_OBJECT_ID", nullable = false)
    private Long id;

    @Column(name = "RENTED_OBJECT_NAME", nullable = false, length = 60)
    private String rentedObjectName;

    @Column(name = "IS_RENTED", nullable = false)
    private boolean isRented;

    @Column(name = "LAST_SETTLEMENT_DATE")
    private LocalDateTime lastSettlementDate;

    @Column(name = "LAST_SETTLEMENT_TOTAL_AMOUNT")
    private BigDecimal lastSettlementTotalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APARTMENT_ID")
    private Apartment apartment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rentedObject")
    private Set<Meter> meters;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rentedObject", cascade = CascadeType.PERSIST)
    private List<Settlement> settlements;

    public RentedObject(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentedObject that = (RentedObject) o;
        return Objects.equals(id, that.id) && Objects.equals(rentedObjectName, that.rentedObjectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rentedObjectName);
    }
}
