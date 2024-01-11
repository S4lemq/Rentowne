package pl.rentowne.rentedObject.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.repository.RentedObjectRepository;
import pl.rentowne.rentedObject.service.RentedObjectService;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RentedObjectServiceImpl implements RentedObjectService {

    private final RentedObjectRepository rentedObjectRepository;

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

}
