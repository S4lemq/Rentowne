package pl.rentowne.user.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.user.model.QUser;
import pl.rentowne.user.model.User;
import pl.rentowne.user.repository.custom.UserRepositoryCustom;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepositoryCustom {
    private static final QUser USER = QUser.user;

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(queryFactory.selectFrom(USER)
                .where(USER.email.eq(email))
                .fetchOne());
    }
}
