package pl.rentowne.security.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.security.model.Token;
import pl.rentowne.security.repository.custom.TokenRepositoryCustom;

import java.util.List;

@Repository
public class TokenRepositoryImpl extends BaseRepositoryImpl<Token, Long> implements TokenRepositoryCustom {

    public TokenRepositoryImpl(EntityManager entityManager) {
        super(Token.class, entityManager);
    }

    @Override
    public List<Token> findAllValidTokensByUser(Long userId) {
//        return queryFactory.selectFrom(TOKEN)
//                .join(USER).on(TOKEN.u)
                return null;
    }
}
