package net.cavitos.documentor.domain.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
public class Document extends RepresentationModel<Document> {
    
    @NotEmpty
    @Size(max = 150)
    private String name;

    @Size(max = 300)
    private String description;

    private List<String> tags;
}
