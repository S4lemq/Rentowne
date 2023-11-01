package pl.rentowne.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.security.model.Token;

import java.util.Collection;
import java.util.List;

/**
 * Tabela użytkowników
 */

@Getter
@Setter
@Builder
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
public class User extends BaseEntity implements UserDetails {
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
    private static final String COL_ROLE = "ROLE";
    private static final String COL_MFA_ENABLED = "MFA_ENABLED";
    private static final String COL_SECRET = "SECRET";

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

    @Enumerated(EnumType.STRING)
    @Column(name = COL_ROLE, nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @Column(name = COL_MFA_ENABLED, nullable = false)
    private boolean mfaEnabled;

    @Column(name = COL_SECRET)
    private String secret;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
