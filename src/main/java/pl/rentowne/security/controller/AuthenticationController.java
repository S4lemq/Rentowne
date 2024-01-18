package pl.rentowne.security.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.security.model.dto.AuthenticationRequest;
import pl.rentowne.security.model.dto.AuthenticationResponse;
import pl.rentowne.security.model.dto.RegisterRequest;
import pl.rentowne.security.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import pl.rentowne.security.service.LogoutService;
import pl.rentowne.security.service.VerificationRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autoryzacja i Autentykacja")
public class AuthenticationController extends AbstractController {

    private final AuthenticationService authService;
    private final LogoutService logoutService;

    @Operation(
            summary = "Rejestracja użytkownika",
            description = "Rejestruje nowego użytkownika w systemie i zwraca dane autentykacji.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Użytkownik został pomyślnie zarejestrowany.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane rejestracyjne."),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera.")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws RentowneBusinessException {
        AuthenticationResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Autentykacja użytkownika",
            description = "Loguje użytkownika i zwraca token autentykacji.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pomyślne zalogowanie użytkownika.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Użytkownik z takim adresem email już istnieje."),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono użytkownika."),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera.")
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws RentowneNotFoundException {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @Operation(
            summary = "Odświeżenie tokena",
            description = "Generuje nowy token dostępu na podstawie istniejącego tokena odświeżania.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Token został pomyślnie odświeżony."),
                    @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane."),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera.")
            }
    )
    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @Operation(
            summary = "Weryfikacja kodu",
            description = "Weryfikuje kod z aplikacji Google Authenticator.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Kod został pomyślnie zweryfikowany."),
                    @ApiResponse(responseCode = "400", description = "Niepoprawny kod."),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera.")
            }
    )
    @PostMapping("/verify")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest verificationRequest) throws RentowneBusinessException {
        return ResponseEntity.ok(authService.verifyCode(verificationRequest));
    }

    @Operation(
            summary = "Wylogowanie użytkownika",
            description = "Wylogowuje użytkownika i unieważnia token autentykacji.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pomyślne wylogowanie użytkownika.",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "401", description = "Brak autoryzacji lub błędny token."),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera.")
            }
    )
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        logoutService.logout(request, null, null);
        return ResponseEntity.ok().build();
    }

}
