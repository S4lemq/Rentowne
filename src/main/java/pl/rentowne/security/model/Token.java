package pl.rentowne.security.model;

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
        name = Token.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(name = Token.TOKEN_PRIMARY_KEY, columnNames = Token.COL_ID),
                @UniqueConstraint(name = Token.TOKEN_UNIQUE_KEY_1, columnNames = Token.COL_TOKEN)
        },
        indexes = {
                @Index(name = Token.TOKEN_INDEX_1, columnList = Token.COL_ID, unique = true),
                @Index(name = Token.TOKEN_INDEX_2, columnList = Token.COL_TOKEN, unique = true)
        }
)
public class Token extends BaseEntity {
    static final String TABLE_NAME = "TOKEN";
    private static final String SEQ_NAME_LC = "token_seq";
    private static final String SEQ_NAME_UC = "TOKEN_SEQ";

    static final String TOKEN_PRIMARY_KEY = "PK_TOKEN";
    static final String TOKEN_UNIQUE_KEY_1 = "UK_TOKEN_1";

    static final String TOKEN_INDEX_1 = "I_TOKEN_1";
    static final String TOKEN_INDEX_2 = "I_TOKEN_2";

    static final String COL_TOKEN = "TOKEN_VALUE";
    static final String COL_TOKEN_TYPE = "TOKEN_TYPE";
    private static final String COL_EXPIRED = "EXPIRED";
    private static final String COL_REVOKED = "REVOKED";
    private static final String COL_USER = "USER_ID";

    @Id
    @SequenceGenerator(name = SEQ_NAME_LC, sequenceName = SEQ_NAME_UC, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME_LC)
    @Column(name = COL_ID, nullable = false)
    private Long id;

    @Column(name = COL_TOKEN, nullable = false, unique = true)
    private String tokenValue;

    @Column(name = COL_TOKEN_TYPE, nullable = false)
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(name = COL_EXPIRED, nullable = false)
    private boolean expired;

    @Column(name = COL_REVOKED, nullable = false)
    private boolean revoked;

    @ManyToOne
    @JoinColumn(name = COL_USER, nullable = false)
    private User user;
}
