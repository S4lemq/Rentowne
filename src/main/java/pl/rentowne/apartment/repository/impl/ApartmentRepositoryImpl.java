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
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.rentedObject.model.QRentedObject;
import pl.rentowne.rentedObject.model.dto.QRentedObjectDto;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Repository
public class ApartmentRepositoryImpl extends BaseRepositoryImpl<Apartment, Long> implements ApartmentRepositoryCustom {

    private static final QApartment apartment = QApartment.apartment;
    private static final QAddress address = QAddress.address;
    private static final QAddressDto addressDto = new QAddressDto(address.id, address.streetName, address.buildingNumber, address.apartmentNumber,
            address.zipCode, address.cityName, address.voivodeship);
    private static final QRentedObject rentedObject = QRentedObject.rentedObject;
    private static final QApartmentDto apartmentDto = new QApartmentDto(apartment.id, apartment.apartmentName,
            apartment.leasesNumber, apartment.isRented, apartment.area, apartment.image, addressDto);
    private static final QRentedObjectDto rentedObjectDto = new QRentedObjectDto(rentedObject.rentedObjectName);

    public ApartmentRepositoryImpl(EntityManager entityManager) {
        super(Apartment.class, entityManager);
    }

    @Override
    public Optional<ApartmentDto> findApartmentById(Long id) throws RentowneNotFoundException {
        ApartmentDto partialApartment = queryFactory.select(apartmentDto)
                .from(apartment)
                .join(address).on(apartment.address().id.eq(address.id))
                .where(apartment.id.eq(id))
                .fetchOne();

        if (partialApartment == null) {
            return Optional.empty();
        }

        Set<RentedObjectDto> rentedObjects = new HashSet<>(queryFactory
                .select(rentedObjectDto)
                .from(rentedObject)
                .where(rentedObject.apartment().id.eq(id))
                .fetch());

        return Optional.of(ApartmentDto.builder()
                .id(partialApartment.getId())
                .apartmentName(partialApartment.getApartmentName())
                .leasesNumber(partialApartment.getLeasesNumber())
                .isRented(partialApartment.isRented())
                .area(partialApartment.getArea())
                .addressDto(partialApartment.getAddressDto())
                .rentedObjectDtos(new ArrayList<>(rentedObjects))
                .image(partialApartment.getImage())
                .build());

    }

}
