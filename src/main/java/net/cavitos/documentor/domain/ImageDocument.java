package net.cavitos.documentor.domain;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private String name;
    private String description;
    private String path;
    private String tenantId;
    private Instant created;
    private Instant updated;
}
