package pl.rentowne.tenant.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.apartment.model.QApartment;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.lease_agreement.model.QLeaseAgreement;
import pl.rentowne.rented_object.model.QRentedObject;
import pl.rentowne.tenant.model.QTenant;
import pl.rentowne.tenant.model.Tenant;
import pl.rentowne.tenant.repository.custom.TenantRepositoryCustom;

import java.util.Optional;

@Repository
public class TenantRepositoryImpl extends BaseRepositoryImpl<Tenant, Long> implements TenantRepositoryCustom {

    private static final QAddress address = QAddress.address;
    private static final QTenant tenant = QTenant.tenant;
    private static final QLeaseAgreement leaseAgreement = QLeaseAgreement.leaseAgreement;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QApartment apartment = QApartment.apartment;

    public TenantRepositoryImpl(EntityManager entityManager) {
        super(Tenant.class, entityManager);
    }

    @Override
    public Optional<Tenant> findByTenantId(Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(tenant)
                        .join(tenant.address(), address).fetchJoin()
                        .join(tenant.leaseAgreement(), leaseAgreement).fetchJoin()
                        .join(tenant.rentedObject(), rentedObject).fetchJoin()
                        .join(rentedObject.apartment(), apartment).fetchJoin()
                        .where(tenant.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public Long getRentedObjectId(Long id) {
        return queryFactory.select(tenant.rentedObject().id)
                .from(tenant)
                .where(tenant.id.eq(id))
                .fetchOne();
    }

    @Override
    public Tenant getTenantByEmail(String loggedEmail) {
        return queryFactory.selectFrom(tenant)
                .where(tenant.email.eq(loggedEmail))
                .fetchOne();
    }
}
