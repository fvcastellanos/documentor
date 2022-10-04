package net.cavitos.documentor.web.validator.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import net.cavitos.documentor.repository.TenantRepository;
import net.cavitos.documentor.web.model.request.DocumentRequest;

@Component
public class NewDocumentValidator implements Validator {

    final private LocalValidatorFactoryBean validator;
    final private TenantRepository tenantRepository;

    @Autowired
    public NewDocumentValidator(LocalValidatorFactoryBean validator, TenantRepository tenantRepository) {

        this.validator = validator;
        this.tenantRepository = tenantRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return DocumentRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        var bindingResult = new BeanPropertyBindingResult(target, errors.getObjectName());

        validator.validate(target, bindingResult);
        errors.addAllErrors(bindingResult);

        if (!errors.hasFieldErrors()) {

            // var document = (NewDocumentRequest) target;
            // validateTenantId(document.getTenantId(), errors);
        }
    } 

    private void validateTenantId(String tenantId, Errors errors) {

        var tenantHolder = tenantRepository.findByTenantId(tenantId);

        if (tenantHolder.isEmpty()) {

            errors.rejectValue("tenantId", "tenantId.not.found", "tenantId not found");
        }
    }
}
