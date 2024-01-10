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
import pl.rentowne.address.model.Address;
import pl.rentowne.apartment.model.Apartment;
import pl.rentowne.apartment.model.dto.AddApartmentDto;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.service.ApartmentService;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.user.model.User;
import pl.rentowne.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mieszkania")
@RequestMapping("/api/apartments")
public class ApartmentController {
    private static final Long EMPTY_ID = null;

    private final ApartmentService apartmentService;
    private final UserService userService;

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
    @GetMapping(value = "/{id}")
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
    @PostMapping()
    public ResponseEntity<ApartmentDto> addApartment(@RequestBody AddApartmentDto apartmentDto) throws RentowneBusinessException {
        Long id = apartmentService.addApartment(mapApartment(apartmentDto, EMPTY_ID));
        return new ResponseEntity<>(apartmentService.getApartment(id), HttpStatus.CREATED);
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
    @PutMapping("/{id}")
    public ResponseEntity<ApartmentDto> updateApartment(@RequestBody AddApartmentDto apartmentDto, @PathVariable Long id) throws RentowneBusinessException {
        apartmentService.updateApartment(mapApartment(apartmentDto, id));
        return ResponseEntity.ok(apartmentService.getApartment(id));
    }

    private Apartment mapApartment(AddApartmentDto apartmentDto, Long apartmentId) throws RentowneBusinessException {
        Long addressId = apartmentService.getAddressId(apartmentId);
        Long loggedUserId = userService.getLoggedUser().getId();
        return Apartment.builder()
                .id(apartmentId)
                .apartmentName(apartmentDto.getApartmentName())
                .leasesNumber(apartmentDto.getLeasesNumber())
                .isRented(apartmentDto.isRented())
                .area(apartmentDto.getArea())
                .user(new User(loggedUserId))
                .address(Address.builder()
                        .id(addressId)
                        .streetName(apartmentDto.getAddressDto().getStreetName())
                        .buildingNumber(apartmentDto.getAddressDto().getBuildingNumber())
                        .apartmentNumber(apartmentDto.getAddressDto().getApartmentNumber())
                        .zipCode(apartmentDto.getAddressDto().getZipCode())
                        .cityName(apartmentDto.getAddressDto().getCityName())
                        .voivodeship(apartmentDto.getAddressDto().getVoivodeship())
                        .build())
                .build();
    }

}
