package pl.rentowne.apartment.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.repository.ApartmentRepository;
import pl.rentowne.apartment.service.ApartmentService;
import pl.rentowne.exception.RentowneNotFoundException;

@Service
@RequiredArgsConstructor
public class ApartmentServiceImpl implements ApartmentService {

    private final ApartmentRepository apartmentRepository;

    @Override
    public ApartmentDto getApartment(Long id) throws RentowneNotFoundException {
        return apartmentRepository.findApartmentById(id).orElseThrow(() -> new RentowneNotFoundException(id));
    }

    @Override
    public Long addApartment(Apartment apartment) {
        return apartmentRepository.save(apartment).getId();
    }

    @Override
    public void updateApartment(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    @Override
    public Long getAddressId(Long id) {
        return apartmentRepository.getAddressId(id);
    }
}
