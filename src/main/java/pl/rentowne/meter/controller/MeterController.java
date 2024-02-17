package pl.rentowne.meter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.meter.model.dto.MeterDto;
import pl.rentowne.meter.service.MeterService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Liczniki")
public class MeterController extends AbstractController {

    private final MeterService meterService;

    @Operation(
            summary = "Zapisuje licznik dla danego obiektu do wynajęcia",
            description = "Metoda służy do zapisu licznika dla danego obiektu do wynajecia",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Licznik dodano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/meter")
    public ResponseEntity<MeterDto> addMeter(@Parameter(description = "dto licznika", required = true) @RequestBody MeterDto meter) {
        return new ResponseEntity<>(meterService.addMeter(meter), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Aktualizuje dane mieszkania",
            description = "Metoda służy do aktualizowania danych mieszkania",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dane licznika zaktualizowano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PutMapping("/api/meter")
    public ResponseEntity<Void> updateMeter(@Parameter(description = "dto licznika", required = true) @RequestBody MeterDto meter) {
        meterService.updateMeter(meter);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Pobiera licznik po id",
            description = "Metoda służy do pobrania licznika po id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Licznik pobrano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping("/api/meter/{id}")
    public ResponseEntity<MeterDto> getMeter(@Parameter(description = "id licznika", required = true) @PathVariable Long id) {
        return new ResponseEntity<>(meterService.getMeterById(id), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Pobiera liczbę liczników danego obiektu",
            description = "Metoda służy do pobrania liczby liczników po id obiektu",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dane pobrano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping("/api/rented-object/{id}/meter-count")
    public ResponseEntity<Void> getMeterCountByRentedObject(@PathVariable Long id) {
        meterService.getMeterCountByRentedObject(id);
        return ResponseEntity.ok().build();
    }
}
