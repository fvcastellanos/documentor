package net.cavitos.documentor.domain.model;

import java.time.Instant;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
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
    @Indexed(name = "idx_documents_name")
    private String name;

    @Size(max = 300)
    private String description;

    @TextIndexed
    private List<String> tags;

    private String path;

    private List<Upload> uploads;

    @NotEmpty
    @Size(max = 50)
    private String tenantId;

    @CreatedDate
    private Instant created;

    @TextScore
    private float score;
}
