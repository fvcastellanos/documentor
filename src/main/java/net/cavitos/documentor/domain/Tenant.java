package net.cavitos.documentor.domain;

import java.time.Instant;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "tenants")
public class Tenant {
    
    @Id
    private String id;

    @NotEmpty
    @Max(200)
    private String name;

    @Max(50)
    @NotEmpty
    private String tenantId;

    @Max(50)
    private String parentTenantId;

    @Email
    @Max(250)
    private String email;

    @CreatedDate
    private Instant created;
}
