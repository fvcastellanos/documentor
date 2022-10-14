package net.cavitos.documentor.domain.web;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class FileUpload extends RepresentationModel<FileUpload> {

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String url;

    @NotEmpty
    @Size(max = 150)
    private String uploadName;
}
