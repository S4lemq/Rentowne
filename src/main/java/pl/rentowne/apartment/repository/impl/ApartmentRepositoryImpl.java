package pl.rentowne.apartment.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.QApartment;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.model.dto.QApartmentDto;
import pl.rentowne.apartment.repository.custom.ApartmentRepositoryCustom;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.rented_object.model.QRentedObject;

import java.util.List;
import java.util.Optional;

@Repository
public class ApartmentRepositoryImpl extends BaseRepositoryImpl<Apartment, Long> implements ApartmentRepositoryCustom {

    private static final QApartment apartment = QApartment.apartment;
    private static final QAddress address = QAddress.address;
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QApartmentDto apartmentDto = new QApartmentDto(apartment.id, apartment.apartmentName);

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

    @Override
    public Long getApartmentIdByRentedObject(Long rentedObjectId) {
         return queryFactory.select(apartment.id)
                 .from(apartment)
                 .join(rentedObject).on(rentedObject.apartment().id.eq(apartment.id))
                 .where(rentedObject.id.eq(rentedObjectId))
                 .fetchOne();
    }

    @Override
    public List<Long> getAllRentedObjectsByApartmentIdAndRentedFlag(Long apartmentId, boolean isRented) {
        return queryFactory.select(rentedObject.id)
                .from(rentedObject)
                .where(rentedObject.isRented.eq(isRented)
                        .and(rentedObject.apartment().id.eq(apartmentId)))
                .fetch();
    }

    @Override
    public void updateRentedFlag(Long apartmentId, boolean flag) {
        queryFactory.update(apartment)
                .set(apartment.isRented, flag)
                .where(apartment.id.eq(apartmentId))
                .execute();
    }

    @Override
    public List<ApartmentDto> getAllApartmentsByLoggedUserAndApartment(Long loggedUser, Long apartmentId) {
        if (apartmentId != null) {
            return queryFactory.select(apartmentDto)
                .from(apartment)
                .where(
                    (
                        apartment.user().id.eq(loggedUser)
                        .and(apartment.isRented.eq(Boolean.FALSE))
                    ).or(
                        apartment.id.eq(apartmentId)
                    )
                )
                .fetch();
        } else {
            return queryFactory.select(apartmentDto)
                .from(apartment)
                .where(apartment.user().id.eq(loggedUser).
                        and(apartment.isRented.eq(Boolean.FALSE)))
                .fetch();
        }
    }

    @Override
    public List<ApartmentDto> getAllApartments(Long id) {
        return queryFactory.select(apartmentDto)
                .from(apartment)
                .where(apartment.user().id.eq(id))
                .fetch();
    }
}
