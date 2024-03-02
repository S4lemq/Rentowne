package pl.rentowne.user.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.user.model.QUser;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.QUserBasicDto;
import pl.rentowne.user.model.dto.QUserDto;
import pl.rentowne.user.model.dto.UserBasicDto;
import pl.rentowne.user.model.dto.UserDto;
import pl.rentowne.user.repository.custom.UserRepositoryCustom;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepositoryCustom {
    private static final QUser user = QUser.user;
    private final QUserBasicDto userBasicDto = new QUserBasicDto(user.id, user.email, user.role);
    private final QUserDto userDto = new QUserDto(user.id, user.firstname, user.lastname, user.email, user.image);

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(queryFactory.selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne());
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        return queryFactory.select(user.id)
                .from(user)
                .where(user.email.eq(email))
                .fetchCount() > 0;
    }


    @Override
    public UserBasicDto getByEmail(String email) {
        return queryFactory.select(userBasicDto)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
    }

    @Override
    public Optional<User> findByHash(String hash) {
        return Optional.ofNullable(
                queryFactory.select(user)
                        .from(user)
                        .where(user.hash.eq(hash))
                        .fetchOne()
        );
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        return queryFactory.select(userDto)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
    }

    @Override
    public String getUserProfileImage(String loggedUserEmail) {
        return queryFactory.select(user.image)
                .from(user)
                .where(user.email.eq(loggedUserEmail))
                .fetchOne();
    }
}
