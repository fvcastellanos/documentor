package net.cavitos.documentor.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "uploads")
public class Upload {

    @Id
    private String id;

    @NotEmpty
    @Size(max = 50)
    private String tenantId;

    @NotEmpty
    private String file;

    private boolean stored;

    @CreatedDate
    private Instant created;
}
