package pl.rentowne.meter.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.meter.model.Meter;
import pl.rentowne.meter.model.dto.MeterDto;
import pl.rentowne.meter.repository.MeterRepository;
import pl.rentowne.meter.service.MeterService;
import pl.rentowne.rentedObject.model.RentedObject;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;

@Service
@RequiredArgsConstructor
public class MeterServiceImpl implements MeterService {

    private final MeterRepository meterRepository;

    @Override
    @Transactional
    public MeterDto addMeter(MeterDto meterDto) {
        RentedObject rentedObjectReference = new RentedObject(meterDto.getRentedObject().getId());

        Meter meter = this.convertToEntity(meterDto, rentedObjectReference);
        Meter savedMeter = meterRepository.save(meter);
        return this.convertToDto(savedMeter);
    }

    @Override
    public MeterDto getMeterById(Long id) {
        Meter meterDto = meterRepository.getMeterDtoById(id);
        return MeterDto.builder()
                .id(meterDto.getId())
                .name(meterDto.getName())
                .meterType(meterDto.getMeterType())
                .rentedObject(RentedObjectDto.builder()
                        .id(meterDto.getRentedObject().getId())
                        .build())
                .build();
    }

    @Override
    @Transactional
    public void updateMeter(MeterDto meter) {
        Meter meterEntity = Meter.builder()
                .id(meter.getId())
                .name(meter.getName())
                .meterType(meter.getMeterType())
                .rentedObject(RentedObject.builder()
                        .id(meter.getRentedObject().getId())
                        .build())
                .build();
        meterRepository.save(meterEntity);
    }

    private Meter convertToEntity(MeterDto dto, RentedObject rentedObjectReference) {
        return Meter.builder()
                .name(dto.getName())
                .meterType(dto.getMeterType())
                .rentedObject(rentedObjectReference)
                .build();
    }

    private MeterDto convertToDto(Meter entity) {
        return MeterDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .meterType(entity.getMeterType())
                .build();
    }
}
