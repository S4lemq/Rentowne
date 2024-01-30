package pl.rentowne.tenant.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.address.model.Address;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;
import pl.rentowne.rentedObject.model.RentedObject;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "TENANT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_TENANT", columnNames = "TENANT_ID"),
                @UniqueConstraint(name = "UK_TENANT_1", columnNames = "ACCOUNT_NUMBER"),
                @UniqueConstraint(name = "UK_TENANT_2", columnNames = "PHONE_NUMBER"),
                @UniqueConstraint(name = "UK_TENANT_3", columnNames = "ADDRESS_ID"),
                @UniqueConstraint(name = "UK_TENANT_4", columnNames = "RENTED_OBJECT_ID")
        },
        indexes = {
                @Index(name = "PK_TENANT", columnList = "TENANT_ID", unique = true),
                @Index(name = "UK_TENANT_1", columnList = "ACCOUNT_NUMBER", unique = true),
                @Index(name = "UK_TENANT_2", columnList = "PHONE_NUMBER", unique = true),
                @Index(name = "UK_TENANT_3", columnList = "ADDRESS_ID", unique = true),
                @Index(name = "UK_TENANT_4", columnList = "RENTED_OBJECT_ID", unique = true),
                @Index(name = "I_TENANT_1", columnList = "EMAIL"),
        }
)
public class Tenant extends BaseEntity {

    @Id
    @SequenceGenerator(name = "tenant_seq", sequenceName = "TENANT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenant_seq")
    @Column(name = "TENANT_ID", nullable = false)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false, length = 50)
    private String firstname;

    @Column(name = "LAST_NAME", nullable = false, length = 50)
    private String lastname;

    @Column(name = "EMAIL", nullable = false, length = 60)
    private String email;

    @Column(name = "ACCOUNT_NUMBER", unique = true, length = 28)
    private String accountNumber;

    @Column(name = "PHONE_NUMBER", unique = true, length = 20)
    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ADDRESS_ID", unique = true)
    private Address address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LEASE_AGREEMENT_ID", unique = true)
    private LeaseAgreement leaseAgreement;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RENTED_OBJECT_ID", unique = true)
    private RentedObject rentedObject;
}
