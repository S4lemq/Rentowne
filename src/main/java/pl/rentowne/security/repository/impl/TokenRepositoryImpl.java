package pl.rentowne.security.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.security.model.QToken;
import pl.rentowne.security.model.Token;
import pl.rentowne.security.model.TokenType;
import pl.rentowne.security.repository.custom.TokenRepositoryCustom;
import pl.rentowne.user.model.QUser;

import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepositoryImpl extends BaseRepositoryImpl<Token, Long> implements TokenRepositoryCustom {
    private static final QToken token = QToken.token;
    private static final QUser user = QUser.user;

    public TokenRepositoryImpl(EntityManager entityManager) {
        super(Token.class, entityManager);
    }

    @Override
    public List<Token> findAllValidTokensByUser(Long userId) {
        return queryFactory.selectFrom(token)
                .join(user).on(token.user().id.eq(user.id))
                .where(user.id.eq(userId).and((token.expired.isFalse()).or(token.revoked.isFalse())))
                .fetch();
    }

    @Override
    public Optional<Token> findByToken(String tokenValue) {
        return Optional.ofNullable(
                queryFactory.selectFrom(token)
                        .where(token.tokenValue.eq(tokenValue))
                        .fetchOne()
        );
    }

    @Override
    public List<Long> findAllExpiredAndRevokedJwtToken() {
        return queryFactory.select(token.id)
                .from(token)
                .where(token.expired.eq(Boolean.TRUE)
                        .and(token.revoked.eq(Boolean.TRUE))
                        .and(token.tokenType.eq(TokenType.BEARER)))
                .fetch();
    }

    @Override
    public void deleteAllByIds(List<Long> tokenIds) {
        queryFactory.delete(token)
                .where(token.id.in(tokenIds))
                .execute();
    }
}
