package net.cavitos.documentor.domain.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.Errors;

import net.cavitos.documentor.domain.response.error.FieldError;

public class ValidationException extends RuntimeException {

    private final List<FieldError> fieldErrors;

    public ValidationException(final Errors errors) {

        this.fieldErrors = buildFieldErrors(errors);
    }

    public List<FieldError> getFieldErrors() {

        return fieldErrors;
    }

    // ------------------------------------------------------------------------------------------------

    private List<FieldError> buildFieldErrors(final Errors errors) {

        return errors.getFieldErrors().stream()
            .map(error -> {

                final var fieldError = new FieldError();
                fieldError.setError(error.getDefaultMessage());
                fieldError.setFieldName(error.getField());
                fieldError.setValue(error.getRejectedValue().toString());

                return fieldError;
            }).collect(Collectors.toList());
    }


}
