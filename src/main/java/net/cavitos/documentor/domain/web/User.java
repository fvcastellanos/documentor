package net.cavitos.documentor.domain.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.domain.validator.ValueOfEnum;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class User extends RepresentationModel<User> {

    @NotEmpty
    @Size(max = 50)
    private String provider;

    @NotEmpty
    @Size(max = 50)
    private String userId;

    private String tenant;

    @ValueOfEnum(enumType = ActiveStatus.class)
    private String status;
}
