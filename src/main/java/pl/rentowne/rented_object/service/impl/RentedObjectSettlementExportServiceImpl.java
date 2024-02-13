package pl.rentowne.rented_object.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.rented_object.service.RentedObjectSettlementExportService;
import pl.rentowne.settlement.model.dto.SettlementExportDto;
import pl.rentowne.settlement.repository.SettlementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentedObjectSettlementExportServiceImpl implements RentedObjectSettlementExportService {

    private final SettlementRepository settlementRepository;

    @Override
    public List<SettlementExportDto> exportSettlements(LocalDateTime from, LocalDateTime to, Long rentedObjectId) {
        return settlementRepository.getSettlementByDateAndRentedObjectId(from, to, rentedObjectId);
    }
}
