package net.cavitos.documentor.web.model.request;

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
public class UpdateTenantRequest {

    @NotEmpty
    @Size(max = 150)
    private String name;
}
