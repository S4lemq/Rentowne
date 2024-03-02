package pl.rentowne.tenant_settlement.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.tenant.model.Tenant;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "TENANT_SETTLEMENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_TENANT_SETTLEMENT", columnNames = "TENANT_SETTLEMENT_ID")
        },
        indexes = {
                @Index(name = "PK_TENANT_SETTLEMENT", columnList = "TENANT_SETTLEMENT_ID", unique = true),
                @Index(name = "UK_TENANT_SETTLEMENT_1", columnList = "ORDER_HASH", unique = true)
        }
)
public class TenantSettlement extends BaseEntity {

    @Id
    @SequenceGenerator(name = "tenant_settlement_seq", sequenceName = "TENANT_SETTLEMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenant_settlement_seq")
    @Column(name = "TENANT_SETTLEMENT_ID", nullable = false)
    private Long id;

    @Column(name = "STATUS", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private TenantSettlementStatus tenantSettlementStatus;

    @Column(name = "GROSS_VALUE", nullable = false)
    private BigDecimal grossValue;

    @Column(name = "ORDER_HASH", length = 12, unique = true)
    private String orderHash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TENANT_ID")
    private Tenant tenant;

    @OneToOne
    @JoinColumn(name = "PAYMENT_ID")
    private Payment payment;
}
