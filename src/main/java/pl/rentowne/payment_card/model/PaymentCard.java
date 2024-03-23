package pl.rentowne.payment_card.model;

import jakarta.persistence.*;
import lombok.*;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.user.model.User;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "PAYMENT_CARD",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_PAYMENT_CARD", columnNames = "PAYMENT_CARD_ID"),
                @UniqueConstraint(name = "UK_PAYMENT_CARD_1", columnNames = "NUMBER"),
                @UniqueConstraint(name = "UK_PAYMENT_CARD_2", columnNames = "USER_ACCOUNT_ID")
        },
        indexes = {
                @Index(name = "PK_PAYMENT_CARD", columnList = "PAYMENT_CARD_ID", unique = true),
                @Index(name = "UK_PAYMENT_CARD_1", columnList = "NUMBER", unique = true),
                @Index(name = "UK_PAYMENT_CARD_2", columnList = "USER_ACCOUNT_ID", unique = true)
        }
)
public class PaymentCard extends BaseEntity {
        @Id
        @SequenceGenerator(name = "payment_card_seq", sequenceName = "PAYMENT_CARD_SEQ", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_card_seq")
        @Column(name = "PAYMENT_CARD_ID", nullable = false)
        private Long id;

        @Column(name = "NUMBER", nullable = false, unique = true, length = 16)
        private String number;

        @Column(name = "NAME", nullable = false, length = 100)
        private String name;

        @Column(name = "MONTH", nullable = false)
        private int month;

        @Column(name = "YEAR", nullable = false)
        private int year;

        @Column(name = "CVV", nullable = false)
        private int cvv;

        @OneToOne
        @JoinColumn(name = "USER_ACCOUNT_ID", unique = true)
        private User user;
}
