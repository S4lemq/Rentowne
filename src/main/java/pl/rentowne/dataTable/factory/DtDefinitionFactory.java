package pl.rentowne.dataTable.factory;

import pl.rentowne.apartment.model.DTApartmentDefinition;
import pl.rentowne.dataTable.dtDefinition.DTDefinition;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DtDefinitionFactory {

    final static Map<String, Supplier<DTDefinition>> DEFINITION_MAP = new HashMap<>();

    static {
        DEFINITION_MAP.put("APARTMENT", DTApartmentDefinition::new);
    }

    public static DTDefinition getDefinition(String definition) throws RentowneBusinessException {
        return Optional.of(DEFINITION_MAP.get(definition))
                .map(Supplier::get)
                .orElseThrow(() -> new RentowneBusinessException(RentowneErrorCode.ITEM_NOT_FOUND, definition));
    }

}
