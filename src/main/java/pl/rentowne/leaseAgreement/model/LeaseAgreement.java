package pl.rentowne.leaseAgreement.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.tenant.model.Tenant;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "LEASE_AGREEMENT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_LEASE_AGREEMENT", columnNames = "LEASE_AGREEMENT_ID")
        },
        indexes = {
                @Index(name = "PK_LEASE_AGREEMENT", columnList = "LEASE_AGREEMENT_ID", unique = true),
                @Index(name = "I_LEASE_AGREEMENT_1", columnList = "IS_CONTRACT_ACTIVE"),
        }
)
public class LeaseAgreement extends BaseEntity {

    @Id
    @SequenceGenerator(name = "lease_agreement_seq", sequenceName = "LEASE_AGREEMENT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lease_agreement_seq")
    @Column(name = "LEASE_AGREEMENT_ID", nullable = false)
    private Long id;

    @Column(name = "START_CONTRACT_DATE", nullable = false)
    private LocalDateTime startContractDate; //od kiedy najem

    @Column(name = "END_CONTRACT_DATE", nullable = false)
    private LocalDateTime endContractDate; //do kiedy najem

    @Column(name = "DEPOSIT", nullable = false)
    private BigDecimal deposit; //kaucja

    @Column(name = "DEPOSIT_PAID", nullable = false)
    private BigDecimal depositPaid; //wpłacona kaucja

    @Column(name = "PAYMENT_DUE_DAY_OF_MONTH", nullable = false)
    private int paymentDueDayOfMonth; //termin płatności

    @Column(name = "RENT_AMOUNT", nullable = false)
    private BigDecimal rentAmount; //kwota czynszu

    @Column(name = "COMPENSATION_AMOUNT", nullable = false)
    private BigDecimal compensationAmount; //kwota odstępnego

    @Column(name = "INTERNET_FEE", nullable = false)
    private BigDecimal internetFee; //kwota za internet

    @Column(name = "GAS_DEPOSIT")
    private BigDecimal gasDeposit; //opłata za gaz

    @Column(name = "INCLUDED_WATER_METERS", nullable = false)
    private BigDecimal includedWaterMeters; //ilość m^3 wliczonej wody

    @Column(name = "INITIAL_ENERGY_METER_READING", nullable = false)
    private BigDecimal initialEnergyMeterReading; //początkowy odczyt energii

    @Column(name = "INITIAL_WATER_METER_READING", nullable = false)
    private BigDecimal initialWaterMeterReading; //początkowy odczyt wody

    @Column(name = "INITIAL_GAS_METER_READING")
    private BigDecimal initialGasMeterReading; //początkowy odczyt gazu

    @Column(name = "DEPOSIT_RETURN_DATE")
    private LocalDateTime depositReturnDate; //data zwrotu kaucji

    @Column(name = "RETURNED_DEPOSIT_AMOUNT")
    private BigDecimal returnedDepositAmount; //kwota zwróconej kaucji

    @Column(name = "IS_CONTRACT_ACTIVE", nullable = false)
    private boolean isContractActive; //czy aktywna umowa

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "leaseAgreement")
    private Tenant tenant;

}
