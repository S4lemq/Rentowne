package pl.rentowne.apartment.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.QApartment;
import pl.rentowne.apartment.repository.custom.ApartmentRepositoryCustom;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.rentedObject.model.QRentedObject;

import java.util.Optional;

@Repository
public class ApartmentRepositoryImpl extends BaseRepositoryImpl<Apartment, Long> implements ApartmentRepositoryCustom {

    private static final QApartment apartment = QApartment.apartment;
    private static final QAddress address = QAddress.address;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;

    public ApartmentRepositoryImpl(EntityManager entityManager) {
        super(Apartment.class, entityManager);
    }

    @Override
    public Optional<Apartment> findApartmentById(Long id) {
        return Optional.ofNullable(
                queryFactory.select(apartment)
                        .from(apartment)
                        .join(apartment.address(), address).fetchJoin()
                        .join(apartment.rentedObjects, rentedObject).fetchJoin()
                        .where(apartment.id.eq(id))
                        .fetchOne()
        );
    }

}
