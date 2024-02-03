package pl.rentowne.service_provider_field.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.housing_service_provider.model.HousingServiceProvider;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "SERVICE_PROVIDER_FIELD",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_SERVICE_PROVIDER_FIELD", columnNames = "SERVICE_PROVIDER_FIELD_ID"),
        },
        indexes = {
                @Index(name = "PK_SERVICE_PROVIDER_FIELD", columnList = "SERVICE_PROVIDER_FIELD_ID", unique = true),
        }
)
public class ServiceProviderField extends BaseEntity {

    @Id
    @SequenceGenerator(name = "service_provider_field_seq", sequenceName = "SERVICE_PROVIDER_FIELD_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_provider_field_seq")
    @Column(name = "SERVICE_PROVIDER_FIELD_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 60)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price;

    @Column(name = "BILLING_METHOD", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private BillingMethod billingMethod;

    @Column(name = "TAX")
    private BigDecimal tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOUSING_SERVICE_PROVIDER_ID")
    private HousingServiceProvider housingServiceProvider;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProviderField that = (ServiceProviderField) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
