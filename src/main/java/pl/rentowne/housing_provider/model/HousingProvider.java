package pl.rentowne.housing_provider.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.provider_field.model.ProviderField;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "HOUSING_SERVICE_PROVIDER",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_HOUSING_SERVICE_PROVIDER", columnNames = "HOUSING_SERVICE_PROVIDER_ID"),
        },
        indexes = {
                @Index(name = "PK_HOUSING_SERVICE_PROVIDER", columnList = "HOUSING_SERVICE_PROVIDER_ID", unique = true),
        }
)
public class HousingProvider extends BaseEntity {

    @Id
    @SequenceGenerator(name = "housing_service_provider_seq", sequenceName = "HOUSING_SERVICE_PROVIDER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "housing_service_provider_seq")
    @Column(name = "HOUSING_SERVICE_PROVIDER_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 45)
    private String name;

    @Column(name = "TYPE", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ProviderType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APARTMENT_ID")
    private Apartment apartment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "housingProvider", cascade = CascadeType.ALL)
    private Set<ProviderField> providerFields = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HousingProvider that = (HousingProvider) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
