package pl.rentowne.security.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.security.model.QToken;
import pl.rentowne.security.model.Token;
import pl.rentowne.security.repository.custom.TokenRepositoryCustom;
import pl.rentowne.user.model.QUser;

import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl extends BaseRepositoryImpl<Token, Long> implements TokenRepositoryCustom {
    private static final QToken TOKEN = QToken.token;
    private static final QUser USER = QUser.user;

    public TokenRepositoryImpl(EntityManager entityManager) {
        super(Token.class, entityManager);
    }

    @Override
    public List<Token> findAllValidTokensByUser(Long userId) {
        return queryFactory.selectFrom(TOKEN)
                .join(USER).on(TOKEN.user().id.eq(USER.id))
                .where(USER.id.eq(userId).and((TOKEN.expired.isFalse()).or(TOKEN.revoked.isFalse())))
                .fetch();
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return Optional.ofNullable(
                queryFactory.selectFrom(TOKEN)
                        .where(TOKEN.tokenValue.eq(token))
                        .fetchOne()
        );
    }

}
