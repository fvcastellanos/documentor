package net.cavitos.documentor.security.configuration;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.repository.UserRepository;
import net.cavitos.documentor.security.service.MongoDbUserService;
import net.cavitos.documentor.security.validator.AudienceValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${security.cors.origins}")
    private String[] origins;


    @Bean
    public MongoDbUserService mongoDbUserService(final UserRepository userRepository,
                                                 final TenantRepository tenantRepository) {

        return new MongoDbUserService(userRepository, tenantRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

        JwtWebSecurityConfigurer
                .forRS256(audience, issuer)
                        .configure(http)
                           .cors(withDefaults())
                            .authorizeRequests()
                            .mvcMatchers(HttpMethod.GET, "/actuator/**").permitAll() // GET requests don't need auth
                            .anyRequest()
                            .authenticated()
                            .and()
                            .oauth2ResourceServer()
                            .jwt()
                            .jwtAuthenticationConverter(jwtAuthenticationConverter())
                            .decoder(jwtDecoder());
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(origins));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("authorization", "content-type"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // ------------------------------------------------------------------------------------------------------

    private JwtDecoder jwtDecoder() {

        final var withAudience = new AudienceValidator(audience);
        final var withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        final var validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

        final var jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);
        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");
        authoritiesConverter.setAuthoritiesClaimName("permissions");

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return converter;
    }

}
