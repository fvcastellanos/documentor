package net.cavitos.documentor.configuration;

import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.web.validator.document.DocumentValidator;
import net.cavitos.documentor.web.validator.tenant.NewTenantRequestValidator;
import net.cavitos.documentor.web.validator.tenant.UpdateTenantRequestValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfiguration {

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {

        return new LocalValidatorFactoryBean();
    }
}
