package net.cavitos.documentor.security.validator;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audience;

    public AudienceValidator(final String audience) {

        this.audience = Objects.requireNonNull(audience, "audience is required");
    }

    @Override
    public OAuth2TokenValidatorResult validate(final Jwt token) {

        final var audiences = token.getAudience();

        if (audiences.contains(this.audience)) {

            return OAuth2TokenValidatorResult.success();
        }

        final var oAuth2Error= new OAuth2Error(OAuth2ErrorCodes.INVALID_TOKEN);
        return OAuth2TokenValidatorResult.failure(oAuth2Error);
    }
}
