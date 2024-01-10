package pl.rentowne.address.model;

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
        name = "ADDRESS",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_ADDRESS", columnNames = "ADDRESS_ID")
        },
        indexes = {
                @Index(name = "PK_ADDRESS", columnList = "ADDRESS_ID", unique = true)
        }
)
public class Address extends BaseEntity {

    @Id
    @SequenceGenerator(name = "address_seq", sequenceName = "ADDRESS_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_seq")
    @Column(name = "ADDRESS_ID", nullable = false)
    private Long id;

    //ulica
    @Column(name = "STREET_NAME", length = 100)
    private String streetName;
    //nr budynku
    @Column(name = "BUILDING_NUMBER", nullable = false, length = 10)
    private String buildingNumber;
    //Nr mieszkania
    @Column(name = "APARTMENT_NUMBER", length = 10)
    private String apartmentNumber;
    //Kod pocztowy
    @Column(name = "ZIP_CODE", nullable = false, length = 10)
    private String zipCode;
    //Miejscowość
    @Column(name = "CITY_NAME", nullable = false, length = 60)
    private String cityName;
    //Województwo
    @Column(name = "VOIVODE_SHIP", nullable = false, length = 60)
    private String voivodeship;
}
