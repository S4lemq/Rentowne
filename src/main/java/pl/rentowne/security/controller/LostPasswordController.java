package pl.rentowne.security.controller;

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
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.security.model.dto.ChangePassword;
import pl.rentowne.security.model.dto.LostPasswordRequest;
import pl.rentowne.security.service.LostPasswordService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Odzyskiwanie hasła")
public class LostPasswordController extends AbstractController {

    private final LostPasswordService lostPasswordService;

    @Operation(
            summary = "Generuje maila do odzyskania hasła",
            description = "Metoda służy do wygenerowania maila w celu odzyskania hasła użytkownika",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Wysłano maila bez względu czy konto istnieje czy nie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/lost-password")
    public ResponseEntity<Void> lostPassword(@RequestBody LostPasswordRequest lostPasswordRequest) {
        lostPasswordService.sendLostPasswordLink(lostPasswordRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Zmienia hasło użytkownika",
            description = "Metoda służy do zmiany hasła użytkownika",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Hasło zmienione"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePassword changePassword) throws RentowneBusinessException {
        lostPasswordService.changePassword(changePassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
