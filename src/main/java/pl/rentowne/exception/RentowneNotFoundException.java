package pl.rentowne.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

/**
 * Klasa wyjątku biznesowego związanego z nieznalezieniem obiektu
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, value = HttpStatus.NOT_FOUND)
public class RentowneNotFoundException extends RentowneBusinessException {

    public RentowneNotFoundException(String key) {
        super(RentowneErrorCode.ITEM_NOT_FOUND, String.format("Searched by key: %s", key));
    }

    public RentowneNotFoundException(String... keys) {
        super(RentowneErrorCode.ITEM_NOT_FOUND, String.format("Searched by keys: %s", Arrays.asList(keys).toString()));
    }

    public RentowneNotFoundException(Long id) {
        super(RentowneErrorCode.ITEM_NOT_FOUND, String.format("Searched by id: %d", id));
    }
}
