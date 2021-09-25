package net.cavitos.documentor.configuration;

// import org.springframework.beans.factory.InitializingBean;
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
// public class ValidatorEventRegister implements InitializingBean {
public class ValidatorEventRegister implements RepositoryRestConfigurer  {
    
    private static final List<String> EVENTS;

    @Autowired
    ListableBeanFactory beanFactory;    

    // @Autowired
    // ValidatingRepositoryEventListener validatingRepositoryEventListener;

    // @Autowired
    // private Map<String, Validator> validators;

    // @Override
    // public void afterPropertiesSet() {
    //     List<String> events = Collections.singletonList("beforeCreate");

    //     validators.forEach((entry, value) -> events.stream()
    //             .filter(entry::startsWith)
    //             .findFirst()
    //             .ifPresent(event -> validatingRepositoryEventListener.addValidator(event, value)));
    // }

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {

        // super.configureValidatingRepositoryEventListener(validatingListener);

        Map<String, Validator> validators = beanFactory.getBeansOfType(Validator.class);

        for (Map.Entry<String, Validator> entry : validators.entrySet()) {

            EVENTS.stream()
                .filter(p -> entry.getKey().startsWith(p))
                .findFirst()
                    .ifPresent(p -> validatingListener.addValidator(p, entry.getValue()));
        }
    }

    static {

        var events = new ArrayList<String>();
        events.add("beforeCreate");
        events.add("afterCreate");
        events.add("beforeSave");
        events.add("afterSave");
        events.add("beforeLinkSave");
        events.add("afterLinkSave");
        events.add("beforeDelete");
        events.add("afterDelete");

        EVENTS = Collections.unmodifiableList(events);
    }    
}
