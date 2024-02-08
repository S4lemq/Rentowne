package pl.rentowne.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.common.model.BaseEntity;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.security.model.Token;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        name = "USER_ACCOUNT",
        uniqueConstraints = {
                @UniqueConstraint(name = "PK_USER_ACCOUNT", columnNames = "USER_ACCOUNT_ID"),
                @UniqueConstraint(name = "UK_USER_ACCOUNT_1", columnNames = "EMAIL")
        },
        indexes = {
                @Index(name = "PK_USER_ACCOUNT", columnList = "USER_ACCOUNT_ID", unique = true),
                @Index(name = "UK_USER_ACCOUNT_1", columnList = "EMAIL", unique = true)
        }
)
public class User extends BaseEntity implements UserDetails {

    @Id
    @SequenceGenerator(name = "user_account_seq", sequenceName = "USER_ACCOUNT_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_account_seq")
    @Column(name = "USER_ACCOUNT_ID", nullable = false)
    private Long id;

    @Column(name = "FIRST_NAME", nullable = false)
    private String firstname;

    @Column(name = "LAST_NAME", nullable = false)
    private String lastname;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 60)
    private String email;

    @Column(name = "PASSWORD", nullable = false, length = 64)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", nullable = false, length = 32)
    private Role role;

    @Column(name = "MFA_ENABLED", nullable = false)
    private boolean mfaEnabled;

    @Column(name = "SECRET")
    private String secret;

    @Column(name = "HASH")
    private String hash;

    @Column(name = "HASH_DATE")
    private LocalDateTime hashDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Apartment> apartments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<HousingProvider> housingProviders;

    public User(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
