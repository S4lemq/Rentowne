package pl.rentowne.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.user.model.dto.UserDto;
import pl.rentowne.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Użytkownik")
public class UserController  extends AbstractController {

    private final UserService userService;

    @Operation(
            summary = "Pobiera użytkownika",
            description = "Metoda zwracająca zalogowanego użytkownika",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sukces", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping(value = "/api/users")
    public ResponseEntity<UserDto> getUser() throws RentowneNotFoundException {
        return ResponseEntity.ok(userService.getFullUserData());
    }

    @Operation(
            summary = "Aktualizuje dane użytkownika",
            description = "Metoda służy do aktualizowania danych użytkownika",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dane użytkownika zaktualizowano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PutMapping("/api/users")
    public ResponseEntity<Void> updateUser(@RequestBody UserDto user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Pobiera nazwę zdjęcia profilowe użytkownika",
        description = "Metoda zwracająca nazwę zdjęcia profilowego użytkownika",
        responses = {
                @ApiResponse(responseCode = "200", description = "Sukces", content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
        }
    )
    @GetMapping(value = "/api/users/profile-image")
    public String getUserProfileImage() throws RentowneNotFoundException {
        return userService.getUserProfileImage();
    }

}
