package net.cavitos.documentor.domain;

import java.time.Instant;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
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
    @Size(max = 200)
    private String name;

    @NotEmpty
    @Size(max = 50)
    @Indexed(unique = true, name = "idx_tenants_tenantId")
    private String tenantId;

    @Size(max = 50)
    private String parentTenantId;

    @Email
    @Size(max = 250)
    @Indexed(unique = true, name = "idx_tenants_email")
    private String email;

    @CreatedDate
    private Instant created;
}

