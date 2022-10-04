package net.cavitos.documentor.web.validator.tenant;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import net.cavitos.documentor.domain.model.Tenant;
import net.cavitos.documentor.web.model.request.NewTenantRequest;

@Component
public class NewTenantRequestValidator implements Validator {

    private LocalValidatorFactoryBean validator;

    public NewTenantRequestValidator(LocalValidatorFactoryBean validator) {

        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        
        return Tenant.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        
        var bindingResult = new BeanPropertyBindingResult(target, errors.getObjectName());

        validator.validate(target, bindingResult);
        validateTenantId(target, errors);

        errors.addAllErrors(bindingResult);
    }

    private void validateTenantId(final Object target, final Errors errors) {

        if (target != null) {

            final var tenant = (NewTenantRequest) target;

            if (tenant != null) {

                final var tenantId = tenant.getTenantId();

                if ((StringUtils.isNotBlank(tenantId)) && (StringUtils.containsIgnoreCase(tenantId, StringUtils.SPACE))) {

                    errors.rejectValue("tenantId", "tenantId.no.spaces", "tenantId shoud not have spaces");
                }    
    
            }
        }
    }
}
