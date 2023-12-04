package pl.rentowne.dataTable.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.rentowne.dataTable.dtDefinition.DTDefinition;
import pl.rentowne.dataTable.dtDefinition.DTRow;
import pl.rentowne.dataTable.enums.DTSortDirection;
import pl.rentowne.dataTable.factory.DtDefinitionFactory;
import pl.rentowne.dataTable.filter.DTFilter;
import pl.rentowne.dataTable.service.DTService;
import pl.rentowne.exception.RentowneBusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class DTFilesController {

    private final DTService dTService;

    public DTFilesController(DTService dTService) {
        this.dTService = dTService;
    }

    @Operation(
            summary = "getItems",
            description = "Metoda zwracająca krotek w bazie danych wg argumentów. Metoda wykorzystywana w tabeli z danymi.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "404", description = "Failure")
            }
    )
    @PostMapping("/items")
    public List<DTRow> getItems(
            @RequestParam(name = "dtDefinition") String dtDefinition,
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "sortDirection", required = false) DTSortDirection sortDirection,
            @RequestParam(name = "sortColumnName", required = false) String sortColumnName,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize,
            @RequestBody(required = false) Map<String, Object> filter) throws RentowneBusinessException {

        DTDefinition definition = DtDefinitionFactory.getDefinition(dtDefinition);
        List<DTFilter> filters = getFilters(filter, text, definition);
        return dTService.getFiles(definition, pageNumber, pageSize, sortDirection, sortColumnName, filters);
    }


    @Operation(
            summary = "getItemsCount",
            description = "Metoda zwracająca liczbę krotek w bazie danych wg argumentów. Metoda wykorzystywana w tabeli z danymi.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class))),
                    @ApiResponse(responseCode = "404", description = "Failure")
            }
    )
    @PostMapping("/items-count")
    public long getItemsCount(
            @RequestParam(name = "dtDefinition") String dtDefinition,
            @RequestParam(name = "text", required = false) String text,
            @RequestBody(required = false) Map<String, Object> filter) throws RentowneBusinessException {

        DTDefinition definition = DtDefinitionFactory.getDefinition(dtDefinition);
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
