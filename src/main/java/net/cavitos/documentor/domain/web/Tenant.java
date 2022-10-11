package net.cavitos.documentor.domain.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.domain.validator.ValueOfEnum;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Tenant extends RepresentationModel<Tenant> {

    @NotEmpty
    @Size(max = 150)
    private String name;

    @NotEmpty
    @Size(max = 50)
    private String tenantId;

    @Email
    @NotEmpty
    @Size(max = 250)
    private String email;

    @ValueOfEnum(enumType = ActiveStatus.class)
    private String status;
}
