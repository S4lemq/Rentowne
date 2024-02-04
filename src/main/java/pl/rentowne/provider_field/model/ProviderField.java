package pl.rentowne.provider_field.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.housing_provider.model.HousingProvider;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "PROVIDER_FIELD",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_PROVIDER_FIELD", columnNames = "PROVIDER_FIELD_ID"),
        },
        indexes = {
                @Index(name = "PK_PROVIDER_FIELD", columnList = "PROVIDER_FIELD_ID", unique = true),
        }
)
public class ProviderField extends BaseEntity {

    @Id
    @SequenceGenerator(name = "provider_field_seq", sequenceName = "PROVIDER_FIELD_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "provider_field_seq")
    @Column(name = "PROVIDER_FIELD_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", length = 60)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "BILLING_METHOD", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private BillingMethod billingMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSING_PROVIDER_ID")
    private HousingProvider housingProvider;
}
