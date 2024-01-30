package pl.rentowne.apartment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.rentowne.apartment.model.dto.ApartmentDto;
import pl.rentowne.apartment.service.ApartmentImageService;
import pl.rentowne.apartment.service.ApartmentService;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.rentedObject.model.dto.RentedObjectDto;
import pl.rentowne.rentedObject.service.RentedObjectService;
import pl.rentowne.user.model.dto.UploadResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mieszkania")
public class ApartmentController extends AbstractController {

    private final ApartmentService apartmentService;
    private final ApartmentImageService apartmentImageService;
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

    @PostMapping("/api/apartments/upload-image")
    public UploadResponse uploadImage(@RequestParam("file") MultipartFile multipartFile) {
        try(InputStream inputStream = multipartFile.getInputStream()) {
            String savedFileName = apartmentImageService.uploadImage(multipartFile.getOriginalFilename(), inputStream);
            return new UploadResponse(savedFileName);
        } catch (IOException e) {
            throw new RuntimeException("Coś poszło źle podczas wgrywania pliku", e);
        }
    }

    @GetMapping("/api/data/apartmentImage/{filename}")
    public ResponseEntity<Resource> serveFiles(@PathVariable String filename) throws IOException {
        Resource file = apartmentImageService.serveFiles(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(Path.of(filename)))
                .body(file);
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

}
