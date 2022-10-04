package net.cavitos.documentor.web.model.request;

import javax.validation.constraints.Email;
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
public class NewTenantRequest {

    @NotEmpty
    @Size(max = 150)
    private String name;

    @NotEmpty
    @Size(max = 50)
    private String tenantId;

    @Size(max = 150)
    private String parentTenantId;

    @Email
    @NotEmpty
    @Size(max = 250)
    private String email;
}
