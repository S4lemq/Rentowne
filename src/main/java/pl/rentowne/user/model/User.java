package pl.rentowne.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.rentowne.common.model.BaseEntity;

/**
 * Tabela użytkowników
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = User.TABLE_NAME,
        uniqueConstraints = {
                @UniqueConstraint(name = User.USER_ACCOUNT_PRIMARY_KEY, columnNames = User.COL_ID),
                @UniqueConstraint(name = User.USER_ACCOUNT_UNIQUE_KEY_1, columnNames = User.COL_EMAIL)
        },
        indexes = {
                @Index(name = User.USER_ACCOUNT_INDEX_1, columnList = User.COL_ID, unique = true),
                @Index(name = User.USER_ACCOUNT_INDEX_2, columnList = User.COL_EMAIL, unique = true)
        }
)
public class User extends BaseEntity {
    static final String TABLE_NAME = "USER_ACCOUNT";
    private static final String SEQ_NAME_LC = "user_account_seq";
    private static final String SEQ_NAME_UC = "USER_ACCOUNT_SEQ";

    static final String USER_ACCOUNT_PRIMARY_KEY = "PK_USER_ACCOUNT";
    static final String USER_ACCOUNT_UNIQUE_KEY_1 = "UK_USER_ACCOUNT_1";

    static final String USER_ACCOUNT_INDEX_1 = "I_USER_ACCOUNT_1";
    static final String USER_ACCOUNT_INDEX_2 = "I_USER_ACCOUNT_2";

    private static final String COL_FIRST_NAME = "FIRST_NAME";
    private static final String COL_LAST_NAME = "LAST_NAME";
    static final String COL_EMAIL = "EMAIL";
    private static final String COL_PASSWORD = "PASSWORD";

    @Id
    @SequenceGenerator(name = SEQ_NAME_LC, sequenceName = SEQ_NAME_UC, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME_LC)
    @Column(name = COL_ID, nullable = false)
    private Long id;

    @Column(name = COL_FIRST_NAME, nullable = false)
    private String firstname;

    @Column(name = COL_LAST_NAME, nullable = false)
    private String lastname;

    @Column(name = COL_EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = COL_PASSWORD, nullable = false)
    private String password;
}
