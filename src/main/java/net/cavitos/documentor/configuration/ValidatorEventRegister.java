package net.cavitos.documentor.configuration;

// import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.validation.Validator;

<<<<<<< HEAD
import java.util.Arrays;
=======
import java.util.ArrayList;
import java.util.Collections;
>>>>>>> b4013541a89ecf5192a474eb2cc6980f914c5d51
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
<<<<<<< HEAD
    public void afterPropertiesSet() throws Exception {

        List<String> events = Arrays.asList("beforeCreate");

        for (Map.Entry<String, Validator> entry : validators.entrySet()) {
            
            events.stream()
              .filter(p -> entry.getKey().startsWith(p))
              .findFirst()
              .ifPresent(
                p -> validatingRepositoryEventListener
               .addValidator(p, entry.getValue()));
=======
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {

        // super.configureValidatingRepositoryEventListener(validatingListener);

        Map<String, Validator> validators = beanFactory.getBeansOfType(Validator.class);

        for (Map.Entry<String, Validator> entry : validators.entrySet()) {

            EVENTS.stream()
                .filter(p -> entry.getKey().startsWith(p))
                .findFirst()
                    .ifPresent(p -> validatingListener.addValidator(p, entry.getValue()));
>>>>>>> b4013541a89ecf5192a474eb2cc6980f914c5d51
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
