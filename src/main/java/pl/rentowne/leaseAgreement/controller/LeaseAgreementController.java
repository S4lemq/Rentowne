package pl.rentowne.leaseAgreement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.leaseAgreement.model.dto.LeaseAgreementDto;
import pl.rentowne.leaseAgreement.service.LeaseAgreementService;
import pl.rentowne.meter.model.dto.MeterDto;

@RestController
@RequiredArgsConstructor
@Tag(name = "Umowy")
public class LeaseAgreementController {

    private final LeaseAgreementService leaseAgreementService;

    @Operation(
            summary = "Zapisuje umowę",
            description = "Metoda służy do zapisu umowy",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Najemcę dodano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/lease-agreements")
    public ResponseEntity<LeaseAgreementDto> addLease(@Parameter(description = "dto umowy", required = true)@RequestBody LeaseAgreementDto leaseAgreementDto) throws RentowneNotFoundException {
        Long leaseAgreementId = leaseAgreementService.addLease(leaseAgreementDto);
        return new ResponseEntity<>(leaseAgreementService.getDto(leaseAgreementId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Pobiera umowę po id",
            description = "Metoda służy do pobrania umowy po id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Umowę pobrano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping("/api/lease-agreements/{id}")
    public ResponseEntity<LeaseAgreementDto> getLeaseAgreement(@Parameter(description = "id licznika", required = true) @PathVariable Long id) throws RentowneNotFoundException {
        return new ResponseEntity<>(leaseAgreementService.getDto(id), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Aktualizuje dane umowy",
            description = "Metoda służy do aktualizowania danych umowy",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dane umowy zaktualizowano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PutMapping("/api/lease-agreements")
    public ResponseEntity<Void> updateLeaseAgreement(@Parameter(description = "dto umowy", required = true) @RequestBody LeaseAgreementDto dto) {
        leaseAgreementService.updateLease(dto);
        return ResponseEntity.ok().build();
    }
}
