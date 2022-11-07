package net.cavitos.documentor.security.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserProfile {

    private String tenant;
    private String userId;
    private String provider;
    private String username;
}
