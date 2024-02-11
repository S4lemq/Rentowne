package pl.rentowne.housing_provider.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.provider_field.model.ProviderField;
import pl.rentowne.user.model.User;

import java.math.BigDecimal;
import java.util.HashSet;
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
        name = "HOUSING_PROVIDER",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_HOUSING_PROVIDER", columnNames = "HOUSING_PROVIDER_ID"),
        },
        indexes = {
                @Index(name = "PK_HOUSING_PROVIDER", columnList = "HOUSING_PROVIDER_ID", unique = true),
        }
)
public class HousingProvider extends BaseEntity {

    @Id
    @SequenceGenerator(name = "housing_provider_seq", sequenceName = "HOUSING_PROVIDER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "housing_provider_seq")
    @Column(name = "HOUSING_PROVIDER_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 45)
    private String name;

    @Column(name = "TYPE", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ProviderType type;

    @Column(name = "TAX")
    private BigDecimal tax;

    @Column(name = "CONVERSION_RATE")
    private BigDecimal conversionRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ACCOUNT_ID")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "housingProvider", cascade = CascadeType.ALL)
    private List<ProviderField> providerFields;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "APARTMENT_HOUSING_PROVIDER",
            joinColumns = {@JoinColumn(name = "HOUSING_PROVIDER_ID")},
            inverseJoinColumns = {@JoinColumn(name = "APARTMENT_ID")}
    )
    private Set<Apartment> apartments = new HashSet<>();

    public void addApartment(Apartment apartment) {
        apartments.add(apartment);
        apartment.getHousingProviders().add(this);
    }

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
