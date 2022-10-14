package net.cavitos.documentor.web.controller.advice;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import net.cavitos.documentor.domain.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.cavitos.documentor.domain.exception.BusinessException;
import net.cavitos.documentor.domain.exception.ValidationException;
import net.cavitos.documentor.domain.web.response.error.ErrorResponse;
import net.cavitos.documentor.domain.web.response.error.FieldError;
import net.cavitos.documentor.domain.web.response.error.ValidationErrorResponse;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<Object> handleFallBackException(Exception exception, WebRequest request) {

        LOGGER.error("unable to process request - ", exception);

        var error = buildErrorResponse("Unable to process request / service unavailable");
        return handleExceptionInternal(exception, error, buildHttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException exception, WebRequest request) {

        LOGGER.error("unable to process request - ", exception);

        var error = buildErrorResponse(exception.getMessage());

        return handleExceptionInternal(exception, error, buildHttpHeaders(),
                exception.getHttpStatus(), request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleRequestValidationException(ValidationException exception, WebRequest request) {

        LOGGER.error("unable to process request because a validation exception - ", exception);

        var error = buildValidationErrorResponse(exception.getFieldErrors());
        return handleExceptionInternal(exception, error, buildHttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException exception, WebRequest request) {

        LOGGER.error("access denied to resource", exception);

        var error = buildErrorResponse(exception.getMessage());
        return handleExceptionInternal(exception, error, buildHttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    // ------------------------------------------------------------------------------------------------

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        LOGGER.error("unable to process request because a validation exception - {}", exception.getMessage());

        final var error = new ValidationErrorResponse();
        error.setMessage("Request validation failed");

        var fieldErrors = exception.getFieldErrors().stream()
                .map(fError -> {

                    final var value = Objects.nonNull(fError.getRejectedValue()) ? fError.getRejectedValue().toString()
                            : "null";

                    final var fieldError = new FieldError();
                    fieldError.setError(fError.getDefaultMessage());
                    fieldError.setFieldName(fError.getField());
                    fieldError.setValue(value);

                    return fieldError;
                }).collect(Collectors.toList());

        error.setErrors(fieldErrors);

        return handleExceptionInternal(exception, error, headers, status, request);
    }

    // ------------------------------------------------------------------------------------------------

    private ErrorResponse buildErrorResponse(String message) {

        var error = new ErrorResponse();
        error.setMessage(message);

        return error;
    }

    private ValidationErrorResponse buildValidationErrorResponse(List<FieldError> errors) {

        final var error = new ValidationErrorResponse();
        error.setErrors(errors);
        error.setMessage("Request validation failed");

        return error;
    }

    private HttpHeaders buildHttpHeaders() {

        return new HttpHeaders();
    }
}
