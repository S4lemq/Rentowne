package pl.rentowne.exception;

import org.antlr.v4.runtime.misc.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Klasa zawierająca kod błędu oraz argumenty
 */
public class RentowneNotification implements Serializable {

    private static final long serialVersionUID = 4642324497908796193L;

    private RentowneErrorCode code;
    private transient Map<String, Object> parameters = new LinkedHashMap<>();

    private RentowneNotification(RentowneErrorCode code, Map<String, Object> parameters) {
        this.code = code;
        this.parameters = parameters;
    }

    public static Map<String, Object> buildParameters(@NotNull RentowneErrorCode code, List<? extends Object> args) {
        Map<String, Object> parameters = new LinkedHashMap<>();

        // brak uprawnień jest przetwarzany inaczej
        if (RentowneErrorCode.NO_PERMISSION == code) {
            parameters.put("data", args);
        } else if (CollectionUtils.isNotEmpty(code.getTranslateKeys())) {
            LinkedList<Object> arguments = new LinkedList<>(args);
            Set<String> translateKeys = code.getTranslateKeys();

            int keysSize = translateKeys.size();
            if (keysSize != arguments.size())
                throw new NotImplementedException(String.format("[RENTOWNE_NOTIFICATION] Error code %s with invalid arguments size %s", code, arguments));

            for (String key : translateKeys) {
                Object argument = Optional.ofNullable(arguments.pollFirst()).orElse(StringUtils.EMPTY);
                if (StringUtils.isNotEmpty(key)) {
                    parameters.put(key, argument);
                }
            }
        }
        return parameters;
    }

    /**
     * Metoda tworząca powiadomienie na podstawie przekazanego kodu błędu oraz opcjonalnych argumentów
     *
     * @param code {@link RentowneErrorCode} kod błędu
     * @param args {@link Object} argumenty błędu
     * @return {@link RentowneNotification}
     */
    public static RentowneNotification buildNotification(@NotNull RentowneErrorCode code, Object ... args) {
        return new RentowneNotification(code, buildParameters(code, Arrays.asList(args)));
    }

    public RentowneErrorCode getCode() {
        return this.code;
    }

    public Map<String, Object> getParameters() {
        return this.parameters;
    }

    @Override
    public String toString() {
        return "RentowneNotification{" +
                "code=" + code +
                ", parameters=" + parameters +
                '}';
    }
}
