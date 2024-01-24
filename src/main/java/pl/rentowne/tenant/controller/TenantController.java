package pl.rentowne.tenant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.tenant.model.dto.TenantDto;
import pl.rentowne.tenant.service.TenantService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Najemcy")
public class TenantController extends AbstractController {

    private final TenantService tenantService;

    @Operation(
            summary = "Zapisuje najemcę",
            description = "Metoda służy do zapisu najemcy",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Najemcę dodano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/tenants")
    public ResponseEntity<TenantDto> addTenant(@RequestBody TenantDto tenantDto) throws RentowneNotFoundException {
        Long tenantId = tenantService.addTenant(tenantDto);
        return new ResponseEntity<>(tenantService.getTenant(tenantId), HttpStatus.CREATED);
    }
}
