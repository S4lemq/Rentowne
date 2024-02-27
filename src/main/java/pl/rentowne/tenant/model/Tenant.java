package pl.rentowne.tenant.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.address.model.Address;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.lease_agreement.model.LeaseAgreement;
import pl.rentowne.rented_object.model.RentedObject;
import pl.rentowne.tenant_settlement.model.TenantSettlement;

import java.util.List;

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
                @UniqueConstraint(name = "UK_TENANT_1", columnNames = "ADDRESS_ID"),
                @UniqueConstraint(name = "UK_TENANT_2", columnNames = "RENTED_OBJECT_ID")
        },
        indexes = {
                @Index(name = "PK_TENANT", columnList = "TENANT_ID", unique = true),
                @Index(name = "UK_TENANT_1", columnList = "ADDRESS_ID", unique = true),
                @Index(name = "UK_TENANT_2", columnList = "RENTED_OBJECT_ID", unique = true),
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

    @Column(name = "ACCOUNT_NUMBER", length = 28)
    private String accountNumber;

    @Column(name = "PHONE_NUMBER", length = 20)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenant")
    private List<TenantSettlement> tenantSettlements;
}
