package pl.rentowne.exception;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Kody wyjątków biznesowych
 */
public enum RentowneErrorCode implements Serializable {
    UNKNOWN_ERROR(),
    ITEM_NOT_FOUND(),
    USER_IS_NOT_LOGGED(),
    NO_PERMISSION("data"),
    USER_ALREADY_EXISTS("email"),
    NON_UNIQUE_RENTED_OBJECT_NAME(),
    WRONG_GOOGLE_AUTH_CODE(),
    BAD_CREDENTIALS(),
    BAD_LINK(),
    LINK_EXPIRED(),
    BAD_METER_READING_VALUES(),
    BAD_METER_READING_DATE(),
    BAD_METER_READING_VALUES_TENANT(),
    BAD_METER_READING_DATE_TENANT(),
    RENTED_OBJECT_DOES_NOT_HAVE_METERS(),
    PROVIDER_TYPE_ALREADY_EXISTS("type"),
    BAD_OLD_PASSWORD(),
    NO_SETTLEMENTS()
    ;

    private Set<String> translateKeys;

    RentowneErrorCode() {
        this.translateKeys = new LinkedHashSet<>();
    }

    RentowneErrorCode(String... translateKey) {
        this.translateKeys = Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(translateKey)));
    }

    public static RentowneErrorCode findByName(String name) {
        return Arrays.stream(RentowneErrorCode.values())
                .filter(error -> error.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Enum: %s value %s not found", RentowneErrorCode.class.getSimpleName(), name)));
    }

    public Set<String> getTranslateKeys() {
        return translateKeys;
    }

    public String getMessageTranslationParam() {
        return "server.error." + name();
    }
}
