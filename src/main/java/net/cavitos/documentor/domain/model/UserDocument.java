package net.cavitos.documentor.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "users")
public class UserDocument {

    @Id
    private String id;

    @Indexed
    @NotEmpty
    @Size(max = 50)
    private String provider;

    @NotEmpty
    @Size(max = 50)
    @Indexed(unique = true)
    private String userId;

    @NotEmpty
    @Size(max = 50)
    @Indexed(name = "idx_users_tenant")
    private String tenantId;

    @NotEmpty
    @Size(max = 20)
    private String status;

    @CreatedDate
    private Instant created;

    private Instant updated;
}
