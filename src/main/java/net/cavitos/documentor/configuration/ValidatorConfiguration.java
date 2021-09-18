package net.cavitos.documentor.configuration;

import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.web.validator.DocumentValidator;
import net.cavitos.documentor.web.validator.TenantValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidatorConfiguration {

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {

        return new LocalValidatorFactoryBean();
    }

    @Bean
    public DocumentValidator beforeCreateDocumentValidator(LocalValidatorFactoryBean localValidatorFactoryBean, 
                                                           TenantRepository tenantRepository) {

        return new DocumentValidator(localValidatorFactoryBean, tenantRepository);
    }

    @Bean
    public TenantValidator beforeCreateTenantValidator(LocalValidatorFactoryBean localValidatorFactoryBean) {

        return new TenantValidator(localValidatorFactoryBean);
    }
}
