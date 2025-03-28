package kg.attractor.job_search.exception.handler;

import jakarta.validation.ConstraintViolationException;
import kg.attractor.job_search.service.ErrorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ErrorService errorService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseBody> handleIAE(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseBody> handleConstraintViolation(ConstraintViolationException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(ex.getConstraintViolations()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponseBody handleNSEE(NoSuchElementException e) {
        log.error(e.getMessage());
        return errorService.makeResponse(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseBody> handleValidation(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(errorService.makeResponse(e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }
}
