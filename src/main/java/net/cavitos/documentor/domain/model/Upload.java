package net.cavitos.documentor.domain.model;

import java.time.Instant;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedDate;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Upload {

    private String id;

    @NotEmpty
    @Size(max = 50)
    private String tenantId;

    @NotEmpty
    private String fileName;

    @NotEmpty
    private String url;

    @NotEmpty
    @Size(max = 150)
    private String uploadName;

    private boolean stored;

    @CreatedDate
    private Instant created;
}
