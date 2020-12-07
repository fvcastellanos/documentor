package net.cavitos.documentor.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UploadResponse {

    private String fileName;
    private String path;    
}
