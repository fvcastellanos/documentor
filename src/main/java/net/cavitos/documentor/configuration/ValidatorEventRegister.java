package net.cavitos.documentor.configuration;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Configuration
public class ValidatorEventRegister implements RepositoryRestConfigurer  {
    
    private static final List<String> EVENTS = buildEvents();

    @Autowired
    private ListableBeanFactory beanFactory;    

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {

        Map<String, Validator> validators = beanFactory.getBeansOfType(Validator.class);

        for (Map.Entry<String, Validator> entry : validators.entrySet()) {

            EVENTS.stream()
                .filter(event -> entry.getKey().startsWith(event))
                .findFirst()
                    .ifPresent(event -> validatingListener.addValidator(event, entry.getValue()));
        }
    }

    private static List<String> buildEvents() {

        var events = new ArrayList<String>();
        events.add("beforeCreate");
        events.add("afterCreate");
        events.add("beforeSave");
        events.add("afterSave");
        events.add("beforeLinkSave");
        events.add("afterLinkSave");
        events.add("beforeDelete");
        events.add("afterDelete");

        return Collections.unmodifiableList(events);
    }
}
