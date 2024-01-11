package pl.rentowne.rentedObject.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.rentedObject.model.QRentedObject;
import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.repository.custom.RentedObjectRepositoryCustom;

import java.util.List;

@Repository
public class RentedObjectRepositoryImpl extends BaseRepositoryImpl<RentedObject, Long> implements RentedObjectRepositoryCustom {

    private static final QRentedObject rentedObject = QRentedObject.rentedObject;

    public RentedObjectRepositoryImpl(EntityManager entityManager) {
        super(RentedObject.class, entityManager);
    }

    @Override
    public List<RentedObject> getAllByApartmentId(Long apartmentId) {
        return queryFactory.selectFrom(rentedObject)
                .where(rentedObject.apartment().id.eq(apartmentId))
                .fetch();
    }
}
