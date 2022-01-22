package net.cavitos.documentor.web.controller.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import net.cavitos.documentor.domain.exception.BusinessException;
import net.cavitos.documentor.domain.response.ErrorResponse;

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

    // ------------------------------------------------------------------------------------------------

    private ErrorResponse buildErrorResponse(String message) {

        var error = new ErrorResponse();
        error.setMessage(message);

        return error;
    }

    private HttpHeaders buildHttpHeaders() {

        return new HttpHeaders();
    }
}
