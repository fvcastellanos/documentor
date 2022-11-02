package net.cavitos.documentor.domain.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.cavitos.documentor.domain.model.status.ActiveStatus;
import net.cavitos.documentor.domain.validator.ValueOfEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UpdateUser {

    @NotEmpty
    @Size(max = 50)
    private String provider;

    @ValueOfEnum(enumType = ActiveStatus.class)
    private String status;

}
