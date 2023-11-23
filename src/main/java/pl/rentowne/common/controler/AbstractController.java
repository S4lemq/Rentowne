package pl.rentowne.common.controler;

import com.beust.jcommander.internal.Nullable;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.rentowne.exception.GeneralExceptionDto;
import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;
import pl.rentowne.exception.RentowneNotFoundException;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Abstrakcyjny kontroller RESTowy
 */
@ControllerAdvice
@RestController
public class AbstractController extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AbstractController.class);

    public AbstractController() {
    }

    /**
     * Przechwytuje i obsługuje wyjątek {@link RentowneBusinessException}
     *
     * @param e       {@link RentowneBusinessException}
     * @param request request
     * @return Obiekt wyjątku
     */
    @ExceptionHandler(RentowneBusinessException.class)
    public ResponseEntity<GeneralExceptionDto> businessExceptionHandle(RentowneBusinessException e, WebRequest request) {
        final GeneralExceptionDto generalExceptionDto = new GeneralExceptionDto(e);
        log.info("RentowneBusinessException- business info - code: {}, number: {}, parameters: {};}", e.getCode(), e.getErrNumber(), e.getParameters());
        log.info("RentowneBusinessException- code: {}, number: {}, context: {}; {}}", e.getCode(), e.getErrNumber(), e.getContextEntries(), e);
        return new ResponseEntity<>(generalExceptionDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Przechwytuje i obsługuje wyjątek {@link RentowneNotFoundException}
     *
     * @param e       {@link RentowneNotFoundException}
     * @param request request
     * @return Obiekt wyjątku
     */
    @ExceptionHandler(RentowneNotFoundException.class)
    public ResponseEntity<GeneralExceptionDto> notFoundExceptionHandle(RentowneNotFoundException e, WebRequest request) {
        log.info("RentowneNotFoundException, error number: {}; {}", e.getErrNumber(), e);
        return new ResponseEntity(new GeneralExceptionDto(e), HttpStatus.NOT_FOUND);
    }

    /**
     * Przechwytuje i obsługuje wyjątek {@link ContextedRuntimeException}
     *
     * @param e       {@link ContextedRuntimeException}
     * @param request request
     * @return Obiekt wyjątku
     */
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ContextedRuntimeException.class)
    public ResponseEntity<GeneralExceptionDto> runtimeExceptionHandle(ContextedRuntimeException e, WebRequest request) {
        long errNumber = ThreadLocalRandom.current().nextLong(10000000, 100000000);
        log.warn("ContextedRuntimeException: {}, stack: {}, cause: {}, error number: {}", ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e),
                ExceptionUtils.getRootCauseMessage(e), errNumber);
        if (e.getCause() instanceof RentowneBusinessException) {
            return businessExceptionHandle((RentowneBusinessException) e.getCause(), request);
        } else if (e.getCause() != null && e.getCause().getCause() instanceof RentowneBusinessException) {
            return businessExceptionHandle((RentowneBusinessException) e.getCause().getCause(), request);
        } else {
            return new ResponseEntity(new GeneralExceptionDto(RentowneErrorCode.UNKNOWN_ERROR, null, e.getMessage(), errNumber),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   /* *//**
     * Przechwytuje i obsługuje wyjątek {@link Exception}
     *
     * @param e       {@link Exception}
     * @param request request
     * @return Obiekt wyjątku
     *//*
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralExceptionDto> generalExceptionHandle(Exception e, WebRequest request) {
        long errNumber = ThreadLocalRandom.current().nextLong(10000000, 100000000);
        if (e instanceof PvmException && e.getCause() instanceof TfmsBusinessException) {
            logException(e, errNumber);
            return businessExceptionHandle((TfmsBusinessException) e.getCause(), request);
        }

        if (e.getCause() instanceof GenericJDBCException && e.getCause().getCause() instanceof SQLException) {

            //FK exception of soft delete
            final SQLException cause = (SQLException) e.getCause().getCause();
            if (cause.getErrorCode() == 20001 && cause.getMessage().startsWith("ORA-20001: integrity constraint")) {
                log.info("ORA-20001: integrity constraint violated - child record found: {}, stack: {}, cause: {}, error number: {}", e, e.getStackTrace(), e.getCause(), errNumber);
                return new ResponseEntity(new GeneralExceptionDto(TfmsErrorCode.FK_INTEGRITY_CONSTRAINT, cause.getStackTrace(), cause.getMessage(), errNumber, resolver.hasSuperAdminPrivilege()),
                        HttpStatus.BAD_REQUEST);
            }

            logException(e, errNumber);
            final String message = cause.getCause() != null ? cause.getCause().getMessage() : cause.getMessage();
            return new ResponseEntity(new GeneralExceptionDto(TfmsErrorCode.SQL_EXCEPTION, e.getStackTrace(), message, errNumber, resolver.hasSuperAdminPrivilege()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (e.getCause() instanceof ConstraintViolationException) {
            final String constraintName = ((ConstraintViolationException) e.getCause()).getConstraintName();
            final TfmsErrorCode errorCode = TfmsErrorCodeUKNamesMapper.getErrorCodeByConstraintName(constraintName);
            if (errorCode != null) {
                final GeneralExceptionDto generalExceptionDto = new GeneralExceptionDto(errorCode, e.getStackTrace(), e.getMessage(), errNumber, resolver.hasSuperAdminPrivilege());
                return new ResponseEntity<>(generalExceptionDto, HttpStatus.BAD_REQUEST);
            }
        }
        logException(e, errNumber);
        return new ResponseEntity(new GeneralExceptionDto(TfmsErrorCode.UNKNOWN_ERROR, e.getStackTrace(), e.getMessage(), errNumber, resolver.hasSuperAdminPrivilege()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
*/
    private void logException(Exception e, long errNumber) {
        log.error("Unhandled Exception: {}, stack: {}, cause: {}, error number: {}", ExceptionUtils.getMessage(e), ExceptionUtils.getStackTrace(e),
                ExceptionUtils.getRootCauseMessage(e), errNumber);
    }

    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        long errNumber = ThreadLocalRandom.current().nextLong(10000000, 100000000);
        logException(ex, errNumber);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

}
