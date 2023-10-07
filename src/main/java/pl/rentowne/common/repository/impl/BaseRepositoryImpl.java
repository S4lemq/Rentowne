package pl.rentowne.common.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import pl.rentowne.common.repository.BaseRepository;
import pl.rentowne.security.model.QToken;

import java.util.function.Supplier;

public abstract class BaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    protected EntityManager entityManager;
    protected JPAQueryFactory queryFactory;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public T findByIdMandatory(ID id) throws IllegalArgumentException {
        return findById(id).orElseThrow(getIllegalArgumentExceptionSupplier(id));
    }

    private Supplier<IllegalArgumentException> getIllegalArgumentExceptionSupplier(ID id) {
        return () -> new IllegalArgumentException("Entity not found with id " + id);
    }
}
