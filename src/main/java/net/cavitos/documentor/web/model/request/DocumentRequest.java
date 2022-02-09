package net.cavitos.documentor.web.model.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DocumentRequest {
    
    @NotEmpty
    @Size(max = 150)
    private String name;

    @Size(max = 300)
    private String description;

    // @NotEmpty
    // @Size(max = 50)
    // private String tenantId;

    private List<String> tags;
}