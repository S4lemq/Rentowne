package pl.rentowne.rentedObject.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.apartment.model.QApartment;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.rentedObject.model.QRentedObject;
import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.model.dto.QRentedObjectDto;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;
import pl.rentowne.rentedObject.repository.custom.RentedObjectRepositoryCustom;
import pl.rentowne.user.model.QUser;

import java.util.List;

@Repository
public class RentedObjectRepositoryImpl extends BaseRepositoryImpl<RentedObject, Long> implements RentedObjectRepositoryCustom {

    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QRentedObjectDto rentedObjectDto = new QRentedObjectDto(rentedObject.id, rentedObject.rentedObjectName);
    private static final QApartment apartment = QApartment.apartment;
    private static final QUser user = QUser.user;


    public RentedObjectRepositoryImpl(EntityManager entityManager) {
        super(RentedObject.class, entityManager);
    }

    @Override
    public List<RentedObject> getAllByApartmentId(Long apartmentId) {
        return queryFactory.selectFrom(rentedObject)
                .where(rentedObject.apartment().id.eq(apartmentId))
                .fetch();
    }

    @Override
    public void deleteAllByApartmentId(Long apartmentId) {
        queryFactory.delete(rentedObject)
                .where(rentedObject.apartment().id.eq(apartmentId))
                .execute();
    }

    @Override
    public List<RentedObjectDto> getAllRentedObjectDtos(Long userId) {
        return queryFactory.select(rentedObjectDto)
                .from(rentedObject)
                .join(apartment).on(rentedObject.apartment().id.eq(apartment.id))
                .join(user).on(apartment.user().id.eq(user.id))
                .where(user.id.eq(userId))
                .fetch();
    }
}
