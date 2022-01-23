package net.cavitos.documentor.domain.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class FieldError {
    
    private String fieldName;
    private String value;
    private String error;
}
