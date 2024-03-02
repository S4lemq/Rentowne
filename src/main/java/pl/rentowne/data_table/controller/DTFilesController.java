package pl.rentowne.data_table.controller;

import org.springframework.security.access.AccessDeniedException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.dt_definition.DTRow;
import pl.rentowne.data_table.enums.DTSortDirection;
import pl.rentowne.data_table.filter.DTFilter;
import pl.rentowne.data_table.service.DTService;
import pl.rentowne.user.model.Role;
import pl.rentowne.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/access/files")
@Tag(name = "Pobieranie tabel")
public class DTFilesController {

    private ApplicationContext applicationContext;
    private final DTService dTService;
    private final UserService userService;

     @Operation(
            summary = "Pobiera krotki według argumentów",
            description = "Metoda zwracająca krotki w bazie danych wg argumentów. Metoda wykorzystywana w tabeli z danymi.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sukces", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping(value = "/items")
    public List<DTRow> getItems(
            @Parameter(description = "Rodzaj tabeli z danymi", required = true) @RequestParam String dtDefinition,
            @Parameter(description = "Tekst, po którym odbywa się wyszukiwanie") @RequestParam(required = false) String text,
            @Parameter(description = "Kierunek sortowania danych") @RequestParam(required = false) DTSortDirection sortDirection,
            @Parameter(description = "Kolumna po jakiej dane mają być sortowane") @RequestParam(required = false) String sortColumnName,
            @Parameter(description = "Numer strony danych", required = true) @RequestParam int pageNumber,
            @Parameter(description = "Rozmiar strony danych", required = true) @RequestParam int pageSize,
            @RequestBody(required = false) Map<String, Object> filter) {

         Role role = userService.getLoggedUser().getRole();
         if (!dtDefinition.equals("TENANT_SETTLEMENT") && role.equals(Role.USER)) {
             throw new AccessDeniedException("Brak uprawnień do tego zasobu.");
         }

         DTDefinition definition = applicationContext.getBean(dtDefinition, DTDefinition.class);
        List<DTFilter> filters = getFilters(filter, text, definition);
        return dTService.getFiles(definition, pageNumber, pageSize, sortDirection, sortColumnName, filters);
    }


    @Operation(
            summary = "Pobiera liczbę krotek według podanych argumentów",
            description = "Metoda zwracająca liczbę krotek w bazie danych wg argumentów. Metoda wykorzystywana w tabeli z danymi.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sukces", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "400", description = "Niepoprawne dane wejściowe"),
                    @ApiResponse(responseCode = "404", description = "Nie znaleziono danych"),
                    @ApiResponse(responseCode = "500", description = "Wewnętrzny błąd serwera")
            }
    )
    @PostMapping("/items-count")
    public long getItemsCount(
            @Parameter(description = "Rodzaj tabeli z danymi") @RequestParam String dtDefinition,
            @Parameter(description = "Tekst, po którym odbywa się wyszukiwanie") @RequestParam(required = false) String text,
            @RequestBody(required = false) Map<String, Object> filter) {
        DTDefinition definition = applicationContext.getBean(dtDefinition, DTDefinition.class);
        List<DTFilter> filters = getFilters(filter, text, definition);
        return dTService.count(definition, filters);
    }


    private List<DTFilter> getFilters(Map<String, Object> filter, String text, DTDefinition definition) {
        List<DTFilter> filters = new ArrayList<>();
        filters.add(DTFilter.buildDTFilter(definition.getUserDefinedFilterColumns(), filter));
        if (StringUtils.isNotEmpty(text)) {
            filters.add(DTFilter.buildDTFilterForFastSearcher(definition.getFastSearcherFilterColumns(), text));
        }
        return filters;
    }
}
