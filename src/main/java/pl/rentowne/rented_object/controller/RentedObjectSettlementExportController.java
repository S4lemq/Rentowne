package pl.rentowne.rented_object.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.rentowne.rented_object.service.RentedObjectSettlementExportService;
import pl.rentowne.settlement.model.dto.SettlementExportDto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Eksport rozliczeń obiektu do wynajęcia")
public class RentedObjectSettlementExportController {

    private static final CSVFormat FORMAT = CSVFormat.Builder
            .create(CSVFormat.DEFAULT)
            .setHeader("Data rozliczenia", "Kwota odstępnego", "Kwota czynszu", "Kwota za internet", "Opłata za gaz", "Kwota za prąd",
                    "Kwota za wodę", "Kwota całkowita")
            .build();

    private final RentedObjectSettlementExportService exportService;

    @GetMapping("/api/rented-objects/{id}/calculate/export")
    public ResponseEntity<Resource> exportSettlements(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
            @PathVariable Long id){
        List<SettlementExportDto> settlements = exportService.exportSettlements(
                LocalDateTime.of(from, LocalTime.of(0, 0, 0)),
                LocalDateTime.of(to, LocalTime.of(23, 59, 59)),
                id);
        ByteArrayInputStream stream = this.transformToCsv(settlements);
        InputStreamResource resource = new InputStreamResource(stream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "objectSettlementExport.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(resource);
    }

    private ByteArrayInputStream transformToCsv(List<SettlementExportDto> settlements) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
             CSVPrinter printer = new CSVPrinter(new PrintWriter(stream), FORMAT)) {
            for (SettlementExportDto settlement : settlements) {
                printer.printRecord(Arrays.asList(
                        settlement.getDate(),
                        settlement.getCompensationAmount(),
                        settlement.getRentAmount(),
                        settlement.getInternetFee(),
                        settlement.getGasDeposit(),
                        settlement.getElectricityAmount(),
                        settlement.getWaterAmount(),
                        settlement.getTotalAmount()
                ));
            }
            printer.flush();
            return new ByteArrayInputStream(stream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Błąd przetwarzania CSV: " + e.getMessage());
        }
    }

}
