package pl.rentowne.rentedObject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;
import pl.rentowne.rentedObject.service.RentedObjectService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Obiekty do wynajęcia")
public class RentedObjectController extends AbstractController {

    private final RentedObjectService rentedObjectService;

    @Operation(
            summary = "Pobiera wszystkie obiekty do wynajęcia dla zalogowanego użytkownika",
            description = "Metoda zwracająca obiekty do wynajęcia dla zalogowanego użytkownika",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sukces", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @GetMapping(value = "/api/rented-objects/all")
    public ResponseEntity<List<RentedObjectDto>> getAllRentedObjects() throws RentowneBusinessException {
        return ResponseEntity.ok(rentedObjectService.getAllRentedObjectDtos());
    }

    @GetMapping(value = "/api/apartments/{id}/rented-objects/all")
    public ResponseEntity<List<RentedObjectDto>> getAllRentedObjectsByApartmentAndAdditionalRentedObject(
            @PathVariable Long id,
            @RequestParam(name = "rentedObjectId", required = false) Long rentedObjectId) throws RentowneBusinessException {
        return ResponseEntity.ok(rentedObjectService.getAllByApartmentAndOptionalRentedObject(id, rentedObjectId));
    }
}
