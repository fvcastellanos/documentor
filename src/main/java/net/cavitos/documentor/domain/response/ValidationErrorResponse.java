package net.cavitos.documentor.domain.response;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ValidationErrorResponse {
    
    private String message;
    private List<FieldError> errors;
}
