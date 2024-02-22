package pl.rentowne.tenant_settlement.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "PAYMENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_PAYMENT", columnNames = "PAYMENT_ID")
        },
        indexes = {
                @Index(name = "PK_PAYMENT", columnList = "PAYMENT_ID", unique = true)
        }
)
public class Payment extends BaseEntity {
    @Id
    @SequenceGenerator(name = "payment_seq", sequenceName = "PAYMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @Column(name = "PAYMENT_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", length = 64, nullable = false)
    private String name;

    @Column(name = "TYPE", length = 32, nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Column(name = "DEFAULT_PAYMENT", nullable = false)
    private boolean defaultPayment;

    @Column(name = "NOTE", nullable = false)
    private String note;

}
