package net.cavitos.documentor.domain;

import java.time.Instant;

import javax.validation.constraints.Email;
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
    private String name;

    @NotEmpty
    private String tenantId;
    private String parentTenantId;

    @Email
    private String email;

    @CreatedDate
    private Instant created;
}
