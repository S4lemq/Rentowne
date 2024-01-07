package pl.rentowne.apartment.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.address.model.Address;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.user.model.User;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "APARTMENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_APARTMENT", columnNames = "APARTMENT_ID")
        },
        indexes = {
                @Index(name = "PK_APARTMENT", columnList = "APARTMENT_ID", unique = true),
                @Index(name = "I_APARTMENT_1", columnList = "APARTMENT_NAME")
        }
)
public class Apartment extends BaseEntity {

    @Id
    @SequenceGenerator(name = "apartment_seq", sequenceName = "APARTMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apartment_seq")
    @Column(name = "APARTMENT_ID", nullable = false)
    private Long id;

    @Column(name = "APARTMENT_NAME", nullable = false, length = 60)
    private String apartmentName;

    @Column(name = "LEASES_NUMBER", nullable = false)
    private BigInteger leasesNumber;

    @Column(name = "AREA", nullable = false)
    private BigDecimal area;

    @Column(name = "IS_RENTED", nullable = false)
    private boolean isRented;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ACCOUNT_ID")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", unique = true)
    private Address address;

    //set nie zadziała gdy dodajesz nowe obiekty, bo porównujesz id, które są nullowe, jak trzeba dodaj UUID lub nowy uniqueContraint z polem
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        return Objects.equals(id, apartment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
