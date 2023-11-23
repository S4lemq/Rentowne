package pl.rentowne.exception;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dto do przekazywania wyjątków do frontu
 */
public class GeneralExceptionDto implements Serializable {

    private RentowneErrorCode code;
    private StackTraceElement[] stackTrace;
    private String message;
    private String field;
    private long errNumber;
    private transient Map<String, Object> parameters = new LinkedHashMap<>();

    public GeneralExceptionDto(RentowneErrorCode code, String message, String field, long errNumber) {
        this.code = code;
        this.field = field;
        this.stackTrace = new StackTraceElement[0];
        if (RentowneErrorCode.UNKNOWN_ERROR.equals(this.code)) {
            this.message = RentowneErrorCode.UNKNOWN_ERROR.name();
        } else {
            this.message = message;
        }
        this.errNumber = errNumber;
    }

    public GeneralExceptionDto(RentowneBusinessException e) {
        this(e.getCode(), e.getMessage(), e.getField(), e.getErrNumber());
        this.parameters = e.getParameters();
    }

    public RentowneErrorCode getCode() {
        return code;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public long getErrNumber() {
        return errNumber;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public String getField() {
        return field;
    }
}