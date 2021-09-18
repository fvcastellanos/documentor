package net.cavitos.documentor.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.validation.Validator;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class ValidatorEventRegister implements InitializingBean {

    @Autowired
    ValidatingRepositoryEventListener validatingRepositoryEventListener;

    @Autowired
    private Map<String, Validator> validators;

    @Override
    public void afterPropertiesSet() {
        List<String> events = Collections.singletonList("beforeCreate");

        validators.forEach((entry, value) -> events.stream()
                .filter(entry::startsWith)
                .findFirst()
                .ifPresent(event -> validatingRepositoryEventListener.addValidator(event, value)));
    }
}
