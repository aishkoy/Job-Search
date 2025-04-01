package kg.attractor.job_search.service.impl;

import jakarta.validation.ConstraintViolation;
import kg.attractor.job_search.exception.handler.ErrorResponseBody;
import kg.attractor.job_search.service.ErrorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartException;

import java.util.*;

@Service
public class ErrorServiceImpl implements ErrorService {
    @Override
    public ErrorResponseBody makeResponse(IllegalStateException e) {
        String message = Optional.ofNullable(e.getMessage())
                .orElse("Invalid state encountered");

        return ErrorResponseBody.builder()
                .title("Illegal State Error")
                .response(Map.of("errors", List.of(message)))
                .build();
    }


    @Override
    public ErrorResponseBody makeResponse(IllegalArgumentException e) {
        String message = Optional.ofNullable(e.getMessage())
                .orElse("Invalid argument provided");

        return ErrorResponseBody.builder()
                .title("Illegal Argument Error")
                .response(Map.of("errors", List.of(message)))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(DataIntegrityViolationException e) {
        String message = e.getMessage();

        return ErrorResponseBody.builder()
                .title("Validation Error")
                .response(Map.of("errors", List.of(message)))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(MultipartException e) {
        String message = Optional.ofNullable(e.getMessage())
                .orElse("Invalid multipart request or file upload");

        return ErrorResponseBody.builder()
                .title("File Upload Error")
                .response(Map.of("errors", List.of(message)))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(Exception e) {
        String message = e.getMessage();
        return ErrorResponseBody.builder()
                .title(message)
                .response(Map.of("errors", List.of(message)))
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(BindingResult bindingResult) {
        Map<String, List<String>> reasons = new HashMap<>();
        bindingResult.getFieldErrors().stream()
                .filter(error -> error.getDefaultMessage() != null)
                .forEach(err -> {
                    if (!reasons.containsKey(err.getField())) {
                        reasons.put(err.getField(), new ArrayList<>());
                    }
                    reasons.get(err.getField()).add(err.getDefaultMessage());
                });
        return ErrorResponseBody.builder()
                .title("Validation Error")
                .response(reasons)
                .build();
    }

    @Override
    public ErrorResponseBody makeResponse(Set<ConstraintViolation<?>> constraintViolations) {
        Map<String, List<String>> reasons = new HashMap<>();
        constraintViolations.forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            reasons.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(errorMessage);
        });

        return ErrorResponseBody.builder()
                .title("Validation Error")
                .response(reasons)
                .build();
    }
}
