package pl.rentowne.tenant_settlement.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "TENANT_SETTLEMENT_LOG",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_TENANT_SETTLEMENT_LOG", columnNames = "TENANT_SETTLEMENT_LOG_ID")
        },
        indexes = {
                @Index(name = "PK_TENANT_SETTLEMENT_LOG", columnList = "TENANT_SETTLEMENT_LOG_ID", unique = true)
        }
)
public class TenantSettlementLog {
    @Id
    @SequenceGenerator(name = "tenant_settlement_log_seq", sequenceName = "TENANT_SETTLEMENT_LOG_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenant_settlement_log_seq")
    @Column(name = "TENANT_SETTLEMENT_LOG_ID", nullable = false)
    private Long id;

    @Column(name = "TENANT_SETTLEMENT_ID")
    private Long tenantSettlementId;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CREATED")
    private LocalDateTime created;
}
