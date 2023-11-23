package pl.rentowne.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.exception.DefaultExceptionContext;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Klasa wyjątku biznesowego
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, value = HttpStatus.BAD_REQUEST)
public class RentowneBusinessException extends Exception {
    private static final long serialVersionUID = 4135296191052175725L;

    //for business information. Will be presented into application (popup - exception-handler)
    private RentowneErrorCode code;
    //for technical information. Will be logged into logger with stack of exception
    private transient Map<String, Object> parameters = new LinkedHashMap<>();
    private DefaultExceptionContext exceptionContext;
    private String field;
    //Error number
    private long errNumber;
    // for JSON serialization
    public RentowneBusinessException() {
        this.errNumber = this.getRandomErrNumber();
    }

    /**
     * Konstruktor wyjątku biznesowego
     * @param code kod wyjątku {@link RentowneErrorCode}
     */
    public RentowneBusinessException(RentowneErrorCode code, String field) {
        this(code, code.toString(), field);
        this.errNumber = this.getRandomErrNumber();
    }

    /**
     * Konstruktor wyjątku biznesowego
     * @param code kod wyjątku {@link RentowneErrorCode}
     * @param message dodatkowa wiadomość wyjątku
     */
    public RentowneBusinessException(RentowneErrorCode code, String message, String field) {
        super(message);
        this.code = code;
        this.field = field;
        this.exceptionContext = new DefaultExceptionContext();
        this.errNumber = this.getRandomErrNumber();
    }

    /**
     * Konstruktor wyjątku biznesowego
     * @param code kod wyjątku {@link RentowneErrorCode}
     * @param message dodatkowa wiadomość wyjątku
     * @param args parametry błędu bieznesowego, które będa mogły być widoczne dla użytkownika.
     *             Z nich budowana jest mapa o kluczu translateKey z {@link RentowneErrorCode} i wartości args
     */
    public RentowneBusinessException(RentowneErrorCode code, String message, String field, List<? extends Object> args) {
        this(code, message, field);
        this.parameters = RentowneNotification.buildParameters(code, args);
        this.errNumber = this.getRandomErrNumber();
    }

    /**
     * Konstruktor wyjątku biznesowego
     * @param code kod wyjątku {@link RentowneErrorCode}
     * @param args parametry błędu bieznesowego, które będa mogły być widoczne dla użytkownika.
     *             Z nich budowana jest mapa o kluczu translateKey z {@link RentowneErrorCode} i wartości args
     */
    public RentowneBusinessException(RentowneErrorCode code, String field, List<? extends Object> args) {
        this(code, field);
        this.parameters = RentowneNotification.buildParameters(code, args);
        this.errNumber = this.getRandomErrNumber();
    }

    public RentowneErrorCode getCode() {
        return code;
    }

    public long getErrNumber() {
        return errNumber;
    }

    public String getField() {
        return field;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Dodaje techniczny informacje
     * @param label etykieta
     * @param value waretość
     * @return instację TfmsBusinessException
     */
    public RentowneBusinessException addContextValue(String label, Object value) {
        this.exceptionContext.addContextValue(label, value);
        return this;
    }

    public RentowneBusinessException setContextValue(String label, Object value) {
        this.exceptionContext.setContextValue(label, value);
        return this;
    }

    private long getRandomErrNumber(){
        return ThreadLocalRandom.current().nextLong(10000000, 100000000);
    }

    @JsonIgnore
    public List<Pair<String, Object>> getContextEntries() {
        return this.exceptionContext.getContextEntries();
    }

    public String getDescription() {
        return String.format("Error code %s error no %s detail message %s", getCode(), getErrNumber(), getMessage());
    }

    @Override
    public String toString() {
        return super.toString() + "; code: " + this.getCode() + "; errNumber: " + this.errNumber + "; context: " + this.getContextEntries();
    }

}
