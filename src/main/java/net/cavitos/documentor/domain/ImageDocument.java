package net.cavitos.documentor.domain;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotEmpty;

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
    private String name;

    private String description;

    private List<String> tags;

    @NotEmpty
    private String path;

    @NotEmpty
    private String tenantId;

    @CreatedDate
    private Instant created;

    @TextScore
    private float score;
}
