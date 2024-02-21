package pl.rentowne.apartment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.rentowne.address.model.Address;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.model.dto.ApartmentHousingProviderRequest;
import pl.rentowne.apartment.repository.ApartmentRepository;
import pl.rentowne.apartment.service.ApartmentService;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.housing_provider.model.HousingProvider;
import pl.rentowne.housing_provider.repository.HousingProviderRepository;
import pl.rentowne.rented_object.model.RentedObject;
import pl.rentowne.rented_object.model.dto.RentedObjectDto;
import pl.rentowne.rented_object.repository.RentedObjectRepository;
import pl.rentowne.rented_object.service.RentedObjectService;
import pl.rentowne.user.model.User;
import pl.rentowne.user.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {
    private static final Long EMPTY_ID = null;

    private final ApartmentRepository apartmentRepository;
    private final UserService userService;
    private final RentedObjectService rentedObjectService;
    private final RentedObjectRepository rentedObjectRepository;
    private final HousingProviderRepository housingProviderRepository;

    @Override
    public ApartmentDto getApartment(Long id) throws RentowneNotFoundException {
        Apartment apartment = apartmentRepository.findApartmentById(id).orElseThrow(() -> new RentowneNotFoundException(id));
        return ApartmentDto.asDto(apartment);
    }

    @Override
    @Transactional
    public Long addApartment(ApartmentDto apartmentDto) throws RentowneBusinessException {
        List<RentedObjectDto> rentedObjectDtos = apartmentDto.getRentedObjectDtos();
        Set<String> uniqueNames = new HashSet<>();
        for (RentedObjectDto dto : rentedObjectDtos) {
            if (!uniqueNames.add(dto.getRentedObjectName())) {
                throw new RentowneBusinessException(RentowneErrorCode.NON_UNIQUE_RENTED_OBJECT_NAME);
            }
        }

        Long savedApartmentId = apartmentRepository.saveAndFlush(mapApartment(apartmentDto, EMPTY_ID)).getId();
        Set<RentedObject> rentedObjects = this.mapToRentedObjectEntities(apartmentDto, savedApartmentId);
        rentedObjectService.saveAll(rentedObjects);
        return savedApartmentId;
    }

    @Override
    public Long getAddressId(Long id) {
        return apartmentRepository.getAddressId(id);
    }

    @Override
    @Transactional
    public void  updateApartment(ApartmentDto apartmentDto, Long apartmentId) throws RentowneBusinessException {
        this.updateRenetObjects(apartmentDto, apartmentId);
        apartmentRepository.save(mapApartment(apartmentDto, apartmentId));
    }

    @Override
    @Transactional
    public void deleteApartment(Long id) {
        rentedObjectService.deleteAllByApartmentId(id);
        apartmentRepository.deleteById(id);
    }

    @Override
    public List<ApartmentDto> getAllApartmentsByLoggedUserAndApartment(Long apartmentId) throws RentowneBusinessException {
        Long loggedUser = userService.getLoggedUser().getId();
        return apartmentRepository.getAllApartmentsByLoggedUserAndApartment(loggedUser, apartmentId);
    }

    @Override
    @Transactional
    public void updateRentedFlagOnApartment(Long rentedObjectId) {
        rentedObjectRepository.updateRentedFlag(rentedObjectId, true);
        Long apartmentId = apartmentRepository.getApartmentIdByRentedObject(rentedObjectId);
        List<Long> ids = apartmentRepository.getAllRentedObjectsByApartmentIdAndRentedFlag(apartmentId, false);
        if (ids.isEmpty()) {
            apartmentRepository.updateRentedFlag(apartmentId, true);
        }
    }

    @Override
    @Transactional
    public void addHousingProviders(ApartmentHousingProviderRequest dto) throws RentowneNotFoundException {
        Apartment apartment = apartmentRepository.getApartmentWithHousingProviders(dto.getApartmentId());
        List<HousingProvider> newHousingProviders = housingProviderRepository.findAllById(dto.getHousingProviderIds());

        Set<HousingProvider> currentHousingProviders = apartment.getHousingProviders();

        // Znajdź dostawców, którzy mają być usunięci (są w DB, ale nie w danych z frontu)
        Set<HousingProvider> providersToRemove = currentHousingProviders.stream()
                .filter(provider -> !newHousingProviders.contains(provider))
                .collect(Collectors.toSet());

        // Usuń nieaktualne powiązania
        for (HousingProvider providerToRemove : providersToRemove) {
            providerToRemove.getApartments().remove(apartment); // Usuń apartament z dostawcy
            apartment.getHousingProviders().remove(providerToRemove); // Usuń dostawcę z apartamentu
        }

        // Dodaj nowe lub zaktualizuj istniejące powiązania
        for (HousingProvider newProvider : newHousingProviders) {
            if (!currentHousingProviders.contains(newProvider)) {
                newProvider.addApartment(apartment); // Ta metoda aktualizuje obie strony relacji
            }
            // Jeśli dostawca już istnieje, nie trzeba nic robić, ponieważ relacja już istnieje
        }
    }

    @Override
    @Transactional
    public void pinApartment(boolean isPinned, long id) {
        apartmentRepository.pinApartment(isPinned, id);
    }

    @Override
    public int getCountOfRentedObjectsInApartment(long rentedObjectId) {
        List<Long> counter = apartmentRepository.getRentedObjectsCountByRentedObjectId(rentedObjectId);
        return counter.size();
    }

    private Apartment mapApartment(ApartmentDto apartmentDto, Long apartmentId) throws RentowneBusinessException {
        Long loggedUserId = userService.getLoggedUser().getId();

        return Apartment.builder()
                .id(apartmentId)
                .apartmentName(apartmentDto.getApartmentName())
                .leasesNumber(apartmentDto.getLeasesNumber())
                .isRented(apartmentDto.isRented())
                .area(apartmentDto.getArea())
                .image(apartmentDto.getImage())
                .user(new User(loggedUserId))
                .address(Address.builder()
                        .id(apartmentDto.getAddressDto().getId())
                        .streetName(apartmentDto.getAddressDto().getStreetName())
                        .buildingNumber(apartmentDto.getAddressDto().getBuildingNumber())
                        .apartmentNumber(apartmentDto.getAddressDto().getApartmentNumber())
                        .zipCode(apartmentDto.getAddressDto().getZipCode())
                        .cityName(apartmentDto.getAddressDto().getCityName())
                        .voivodeship(apartmentDto.getAddressDto().getVoivodeship())
                        .build())
                .build();
    }

    private void updateRenetObjects(ApartmentDto apartmentDto, Long apartmentId) {
        if (apartmentId != null) {
            Set<RentedObject> rentedObjects = this.mapToRentedObjectEntities(apartmentDto, apartmentId);
            List<RentedObject> rentedObjectsFromDB = rentedObjectService.getAllByApartmentId(apartmentId);

            Set<String> namesFromFront = rentedObjects.stream()
                    .map(RentedObject::getRentedObjectName)
                    .collect(Collectors.toSet());

            Set<String> namesFromDb = rentedObjectsFromDB.stream()
                    .map(RentedObject::getRentedObjectName)
                    .collect(Collectors.toSet());

            // Obiekty do usunięcia - są w bazie danych, ale nie ma ich w danych z frontu
            List<RentedObject> toDelete = rentedObjectsFromDB.stream()
                    .filter(entity -> !namesFromFront.contains(entity.getRentedObjectName()))
                    .toList();

            // Obiekty do dodania - są w danych z frontu, ale nie ma ich w bazie danych
            List<RentedObject> toAdd = rentedObjects.stream()
                    .filter(entity -> !namesFromDb.contains(entity.getRentedObjectName()))
                    .toList();


            rentedObjectService.deleteAll(toDelete);
            rentedObjectService.saveAll(new HashSet<>(toAdd));
        }
    }

    private Set<RentedObject> mapToRentedObjectEntities(ApartmentDto apartmentDto, Long savedApartmentId) {
        return apartmentDto.getRentedObjectDtos().stream()
                .map(dto -> RentedObject.builder()
                        .rentedObjectName(dto.getRentedObjectName())
                        .isRented(false)
                        .apartment(new Apartment(savedApartmentId))
                        .build())
                .collect(Collectors.toSet());
    }

}
