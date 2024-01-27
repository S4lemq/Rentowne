package pl.rentowne.tenant.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.leaseAgreement.model.QLeaseAgreement;
import pl.rentowne.tenant.model.QTenant;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.repository.custom.TenantRepositoryCustom;

import java.util.Optional;

@Repository
public class TenantRepositoryImpl extends BaseRepositoryImpl<Tenant, Long> implements TenantRepositoryCustom {

    private static final QAddress address = QAddress.address;
    private static final QTenant tenant = QTenant.tenant;
    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;

    public TenantRepositoryImpl(EntityManager entityManager) {
        super(Tenant.class, entityManager);
    }

    @Override
    public Optional<Tenant> findByTenantId(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(tenant)
                        .join(tenant.address(), address).fetchJoin()
                        .join(tenant.leaseAgreement(), leaseAgreement).fetchJoin()
                        .where(tenant.id.eq(id))
                        .fetchOne()
        );
    }

}
