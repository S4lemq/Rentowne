package pl.rentowne.rentedObject.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.meter.model.dto.MeterDto;
import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;
import pl.rentowne.rentedObject.repository.RentedObjectRepository;
import pl.rentowne.rentedObject.service.RentedObjectService;
import pl.rentowne.user.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RentedObjectServiceImpl implements RentedObjectService {

    private final RentedObjectRepository rentedObjectRepository;
    private final UserService userService;

    @Override
    public void saveAll(Set<RentedObject> rentedObjects) {
        rentedObjectRepository.saveAll(rentedObjects);
    }

    @Override
    public List<RentedObject> getAllByApartmentId(Long apartmentId) {
        return rentedObjectRepository.getAllByApartmentId(apartmentId);
    }

    @Override
    public void deleteAll(List<RentedObject> toDelete) {
        rentedObjectRepository.deleteAll(toDelete);
    }

    @Override
    public void deleteAllByApartmentId(Long apartmentId) {
        rentedObjectRepository.deleteAllByApartmentId(apartmentId);
    }

    @Override
    public List<RentedObjectDto> getAllRentedObjectDtosWithMeters(Long apartmentId) {
        List<RentedObject> rentedObjects = rentedObjectRepository.findByApartmentIdWithMeters(apartmentId);
        return this.mapToDtos(rentedObjects);
    }

    @Override
    public List<RentedObjectDto> getAllRentedObjectDtos() throws RentowneBusinessException {
        Long userId = userService.getLoggedUser().getId();
        return rentedObjectRepository.getAllRentedObjectDtos(userId);
    }

    private List<RentedObjectDto> mapToDtos(List<RentedObject> rentedObjects) {
        return rentedObjects.stream()
                .map(ro -> {
                    List<MeterDto> meterDtos = ro.getMeters().stream()
                            .map(meter -> MeterDto.builder()
                                    .id(meter.getId())
                                    .name(meter.getName())
                                    .meterType(meter.getMeterType())
                                    .build())
                            .collect(Collectors.toList());
                    return RentedObjectDto.builder()
                            .id(ro.getId())
                            .rentedObjectName(ro.getRentedObjectName())
                            .meters(meterDtos)
                            .build();
                })
                .collect(Collectors.toList());
    }

}
