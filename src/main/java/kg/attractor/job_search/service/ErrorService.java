package kg.attractor.job_search.service;

import jakarta.validation.ConstraintViolation;
import kg.attractor.job_search.exception.handler.ErrorResponseBody;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindingResult;

import java.util.Set;

public interface ErrorService {
    ErrorResponseBody makeResponse(DataIntegrityViolationException e);

    ErrorResponseBody makeResponse(Exception e);

    ErrorResponseBody makeResponse(BindingResult bindingResult);

    ErrorResponseBody makeResponse(Set<ConstraintViolation<?>> constraintViolations);

}
