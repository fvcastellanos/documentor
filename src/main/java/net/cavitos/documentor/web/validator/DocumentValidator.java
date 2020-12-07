package net.cavitos.documentor.web.validator;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import net.cavitos.documentor.domain.ImageDocument;

public class DocumentValidator implements Validator {

    private LocalValidatorFactoryBean validator;

    public DocumentValidator(LocalValidatorFactoryBean validator) {

        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return ImageDocument.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        var bindingResult = new BeanPropertyBindingResult(target, errors.getObjectName());

        validator.validate(target, bindingResult);
        errors.addAllErrors(bindingResult);
    } 
}
