package net.cavitos.documentor.web.validator.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import net.cavitos.documentor.web.model.request.UpdateDocumentRequest;

public class UpdateDocumentValidator implements Validator {

    final private LocalValidatorFactoryBean validator;

    @Autowired
    public UpdateDocumentValidator(LocalValidatorFactoryBean validator) {

        this.validator = validator;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return UpdateDocumentRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        
    }
    
}
