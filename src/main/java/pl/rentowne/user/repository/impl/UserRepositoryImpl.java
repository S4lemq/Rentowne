package pl.rentowne.user.repository.impl;

import com.querydsl.core.Tuple;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.payment_card.model.QPaymentCard;
import pl.rentowne.user.model.QUser;
import pl.rentowne.user.model.User;
import pl.rentowne.user.model.dto.QUserBasicDto;
import pl.rentowne.user.model.dto.UserBasicDto;
import pl.rentowne.user.repository.custom.UserRepositoryCustom;

import java.util.Optional;

@Repository
public class UserRepositoryImpl extends BaseRepositoryImpl<User, Long> implements UserRepositoryCustom {
    private static final QUser user = QUser.user;
    private static final QAddress address = QAddress.address;
    private static final QPaymentCard paymentCard = QPaymentCard.paymentCard;
    private final QUserBasicDto userBasicDto = new QUserBasicDto(user.id, user.email, user.role);

    public UserRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(
                queryFactory.select(user)
                .from(user)
                .where(user.email.eq(email))
                .fetchOne()
        );
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
    public User getFullUserDataByEmail(String loggedUserEmail) {
        return queryFactory.selectFrom(user)
                .leftJoin(user.address(), address).fetchJoin()
                .leftJoin(user.paymentCard(), paymentCard).fetchJoin()
                .where(user.email.eq(loggedUserEmail))
                .fetchOne();
    }

    @Override
    public Tuple getUserProfileImageAndLang(String loggedUserEmail) {
        return queryFactory.select(user.preferredLanguage, user.image)
                .from(user)
                .where(user.email.eq(loggedUserEmail))
                .fetchOne();
    }
}
