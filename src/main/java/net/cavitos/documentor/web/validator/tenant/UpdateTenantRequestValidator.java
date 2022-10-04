package net.cavitos.documentor.web.validator.tenant;

import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import net.cavitos.documentor.web.model.request.UpdateTenantRequest;

@Component
public class UpdateTenantRequestValidator implements Validator {

    private final LocalValidatorFactoryBean validator;

    public UpdateTenantRequestValidator(final LocalValidatorFactoryBean validator) {

        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return UpdateTenantRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        var bindingResult = new BeanPropertyBindingResult(target, errors.getObjectName());

        validator.validate(target, bindingResult);
        errors.addAllErrors(bindingResult);
    }
    
}
