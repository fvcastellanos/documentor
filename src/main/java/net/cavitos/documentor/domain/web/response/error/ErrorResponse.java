package net.cavitos.documentor.domain.web.response.error;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ErrorResponse {
    
    private String message;
}
