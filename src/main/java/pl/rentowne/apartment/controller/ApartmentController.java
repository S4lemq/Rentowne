package pl.rentowne.apartment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.model.dto.ApartmentHousingProviderRequest;
import pl.rentowne.apartment.service.ApartmentService;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.rented_object.model.dto.RentedObjectDto;
import pl.rentowne.rented_object.service.RentedObjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mieszkania")
public class ApartmentController extends AbstractController {

    private final ApartmentService apartmentService;
    private final RentedObjectService rentedObjectService;

    @Operation(
            summary = "Pobiera mieszkanie",
            description = "Metoda zwracająca mieszkanie po wskazanym id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sukces", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping(value = "/api/apartments/{id}")
    public ResponseEntity<ApartmentDto> getApartment(@Parameter(description = "id mieszkania", required = true) @PathVariable Long id) throws RentowneNotFoundException {
        return ResponseEntity.ok(apartmentService.getApartment(id));
    }

    @Operation(
            summary = "Zapisuje mieszkanie",
            description = "Metoda służy do zapisu mieszkania",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Mieszkanie dodano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/apartments")
    public ResponseEntity<ApartmentDto> addApartment(@RequestBody ApartmentDto apartmentDto) throws RentowneBusinessException {
        Long apartmentId = apartmentService.addApartment(apartmentDto);
        return new ResponseEntity<>(apartmentService.getApartment(apartmentId), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Aktualizuje dane mieszkania",
            description = "Metoda służy do aktualizowania danych mieszkania",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dane mieszkania zaktualizowano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PutMapping("/api/apartments/{id}")
    public ResponseEntity<ApartmentDto> updateApartment(@RequestBody ApartmentDto apartmentDto, @PathVariable Long id) throws RentowneBusinessException {
        apartmentService.updateApartment(apartmentDto, id);
        return ResponseEntity.ok(apartmentService.getApartment(id));
    }

    @Operation(
            summary = "Usuwa mieszkanie",
            description = "Metoda służy do usunięcia mieszkania",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mieszkanie zostało usunięte pomyślnie"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @DeleteMapping("/api/apartments/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Pobiera obiekty do wynajęcia wraz z licznikami",
            description = "Metoda zwracająca obiekty do wynajęcia z licznikami po id mieszkania",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sukces", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping(value = "/api/apartments/{id}/rented-objects/meters")
    public ResponseEntity<List<RentedObjectDto>> getRentedObjectsWithMeters(@Parameter(description = "id mieszkania", required = true) @PathVariable Long id) {
        return ResponseEntity.ok(rentedObjectService.getAllRentedObjectDtosWithMeters(id));
    }

    @GetMapping(value = "/api/apartments/all-by-logged-user")
    public ResponseEntity<List<ApartmentDto>> getAllApartmentsByLoggedUser(
            @RequestParam(name = "apartmentId", required = false) Long apartmentId
    ) throws RentowneBusinessException {
        return ResponseEntity.ok(apartmentService.getAllApartmentsByLoggedUserAndApartment(apartmentId));
    }

    @Operation(
            summary = "Przypisuje dostawcę do mieszkania",
            description = "Metoda służy do zapisu mieszkania",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dostawcę przypisano pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/apartments/add-housing-provider")
    public ResponseEntity<Void> addHousingProvider(@RequestBody ApartmentHousingProviderRequest dto) throws RentowneNotFoundException {
        this.apartmentService.addHousingProviders(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Służy do przypinania nieruchomości",
            description = "Metoda służy do przypinania nieruchomości",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Mieszkanie przypięto pomyślnie"),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/api/apartments/{id}/pin")
    public ResponseEntity<Void> pinApartment(@RequestParam boolean isPinned, @PathVariable long id) {
        apartmentService.pinApartment(isPinned, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
