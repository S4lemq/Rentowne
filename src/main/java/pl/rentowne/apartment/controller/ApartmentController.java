package pl.rentowne.apartment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {


    @GetMapping()
    public ResponseEntity<ApartmentDto> getAvailableApartmentNames() {
        return ResponseEntity.ok(new ApartmentDto("Poland", "Łabiszyńska"));
    }


}
