package pl.rentowne.meter_reading.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.meter.model.Meter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "METER_READING",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_METER_READING", columnNames = "METER_READING_ID"),
        },
        indexes = {
                @Index(name = "PK_METER_READING", columnList = "METER_READING_ID", unique = true),
        }
)
public class MeterReading extends BaseEntity {

    @Id
    @SequenceGenerator(name = "meter_reading_seq", sequenceName = "METER_READING_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meter_reading_seq")
    @Column(name = "METER_READING_ID", nullable = false)
    private Long id;

    @Column(name = "CURRENT_READING", nullable = false)
    private BigDecimal currentReading; //bieżący odczyt w danym miesiącu

    @Column(name = "READING_DATE", nullable = false)
    private LocalDateTime readingDate; //data odczytu

    @Column(name = "CONSUMPTION", nullable = false)
    private BigDecimal consumption; //zużycie w danym miesiącu

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "METER_ID")
    private Meter meter;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterReading that = (MeterReading) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
