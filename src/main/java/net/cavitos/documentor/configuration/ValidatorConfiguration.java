package net.cavitos.documentor.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import net.cavitos.documentor.web.validator.DocumentValidator;
import net.cavitos.documentor.web.validator.TenantValidator;

@Configuration
public class ValidatorConfiguration {

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {

        return new LocalValidatorFactoryBean();
    }

    @Bean
    public DocumentValidator beforeCreateDocumentValidator(LocalValidatorFactoryBean localValidatorFactoryBean) {

        return new DocumentValidator(localValidatorFactoryBean);
    }

    @Bean
    public TenantValidator beforeCreateTenantValidator(LocalValidatorFactoryBean localValidatorFactoryBean) {

        return new TenantValidator(localValidatorFactoryBean);
    }
}
