package pl.rentowne.apartment.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.address.model.dto.QAddressDto;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.QApartment;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.model.dto.QApartmentDto;
import pl.rentowne.apartment.repository.custom.ApartmentRepositoryCustom;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;

import java.util.Optional;

@Repository
public class ApartmentRepositoryImpl extends BaseRepositoryImpl<Apartment, Long> implements ApartmentRepositoryCustom {

    private static final QApartment apartment = QApartment.apartment;
    private static final QAddress address = QAddress.address;
    private static final QAddressDto addressDto = new QAddressDto(address.id, address.streetName, address.buildingNumber, address.apartmentNumber,
            address.zipCode, address.cityName, address.voivodeship);
    private static final QApartmentDto apartmentDto = new QApartmentDto(apartment.id, apartment.apartmentName,
            apartment.leasesNumber, apartment.isRented, apartment.area, addressDto);

    public ApartmentRepositoryImpl(EntityManager entityManager) {
        super(Apartment.class, entityManager);
    }

    @Override
    public Optional<ApartmentDto> findApartmentById(Long id) {
        return Optional.ofNullable(queryFactory.select(apartmentDto)
                .from(apartment)
                .join(address).on(apartment.address().id.eq(address.id))
                .where(apartment.id.eq(id))
                .fetchOne());
    }
}
