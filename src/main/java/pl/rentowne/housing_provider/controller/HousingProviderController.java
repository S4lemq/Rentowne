package pl.rentowne.housing_provider.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.housing_provider.model.dto.HousingProviderDto;
import pl.rentowne.housing_provider.service.HousingProviderService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Dostawcy świadczeń")
public class HousingProviderController {

    private final HousingProviderService housingProviderService;

    @Operation(
            summary = "Zapisuje dostawcę świadczeń",
            description = "Metoda służy do zapisu dostawcy świadczeń",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dostawcę świadczeń dodano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/housing-service-provider")
    public ResponseEntity<Void> addHousingServiceProvider(@RequestBody HousingProviderDto dto) {
        housingProviderService.addHousingServiceProvider(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Pobiera dostawcę po id",
            description = "Metoda służy do pobrania dostawcy po id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dostawcę pobrano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping("/api/housing-service-provider/{id}")
    public ResponseEntity<HousingProviderDto> getHousingServiceProvider(@PathVariable Long id) {
        return new ResponseEntity<>(housingProviderService.getHousingProviderById(id), HttpStatus.CREATED);
    }

}
