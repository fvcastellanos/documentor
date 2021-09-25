package net.cavitos.documentor.domain;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.TextScore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "documents")
public class ImageDocument {
    
    @Id
    private String id;

    @NotEmpty
    @Size(max = 150)
    private String name;

    @Size(max = 300)
    private String description;

    private List<String> tags;

    @NotEmpty
    private String path;

    @NotEmpty
    @Size(max = 50)
    private String tenantId;

    @CreatedDate
    private Instant created;

    @TextScore
    private float score;
}
