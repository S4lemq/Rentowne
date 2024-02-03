package pl.rentowne.meter.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.meter_reading.model.MeterReading;
import pl.rentowne.rented_object.model.RentedObject;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "METER",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_METER", columnNames = "METER_ID")
        },
        indexes = {
                @Index(name = "PK_METER", columnList = "METER_ID", unique = true)
        }
)
public class Meter extends BaseEntity {

    @Id
    @SequenceGenerator(name = "meter_seq", sequenceName = "METER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meter_seq")
    @Column(name = "METER_ID", nullable = false)
    private Long id;

    @Column(name = "NAME", nullable = false, length = 32)
    private String name;

    @Column(name = "TYPE", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private MeterType meterType;

    @Column(name = "METER_NUMBER", length = 20)
    private String meterNumber;

    @Column(name = "INITIAL_METER_READING")
    private BigDecimal initialMeterReading;

    @Column(name = "INSTALLATION_DATE")
    private LocalDateTime installationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RENTED_OBJECT_ID")
    private RentedObject rentedObject;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "meter")
    private Set<MeterReading> meterReadings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meter meter = (Meter) o;
        return Objects.equals(id, meter.id) && Objects.equals(name, meter.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
