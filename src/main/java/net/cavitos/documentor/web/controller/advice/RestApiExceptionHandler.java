package net.cavitos.documentor.web.controller.advice;

import java.util.List;

import com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.cavitos.documentor.domain.exception.BusinessException;
import net.cavitos.documentor.domain.exception.ValidationException;
import net.cavitos.documentor.domain.response.error.ErrorResponse;
import net.cavitos.documentor.domain.response.error.FieldError;
import net.cavitos.documentor.domain.response.error.ValidationErrorResponse;

@ControllerAdvice
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiExceptionHandler.class);

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

    // @ExceptionHandler(RepositoryConstraintViolationException.class)
    // public ResponseEntity<Object> handleValidationException(RepositoryConstraintViolationException exception, WebRequest request) {

    //     LOGGER.error("unable to process request because a validation exception - ", exception);

    //     return handleExceptionInternal(exception, buildValidationErrorResponse(exception), buildHttpHeaders(), 
    //         HttpStatus.BAD_REQUEST, request);
    // }

    // ------------------------------------------------------------------------------------------------

    private ErrorResponse buildErrorResponse(String message) {

        var error = new ErrorResponse();
        error.setMessage(message);

        return error;
    }

    private ValidationErrorResponse buildValidationErrorResponse(List<FieldError> errors) {

        final var error = new ValidationErrorResponse();
        error.setErrors(errors);
        error.setMessage("Request validation has failed");

        return error;
    }

    private ValidationErrorResponse buildValidationErrorResponse(RepositoryConstraintViolationException exception) {

        final List<FieldError> errors = Lists.newArrayList();

        final var error = new ValidationErrorResponse();
        error.setMessage(exception.getMessage());
        error.setErrors(errors);

        if (exception.getErrors().hasFieldErrors()) {

            exception.getErrors().getFieldErrors()
                .forEach(fieldError -> {

                    final var fError = new FieldError();
                    fError.setFieldName(fieldError.getField());
                    fError.setValue(fieldError.getRejectedValue().toString());
                    fError.setError(fieldError.getCode());

                    errors.add(fError);
                });
        }

        return error;
    }

    private HttpHeaders buildHttpHeaders() {

        return new HttpHeaders();
    }
}
