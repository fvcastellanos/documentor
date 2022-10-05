package net.cavitos.documentor.web.controller;

import org.springframework.validation.BeanPropertyBindingResult;

import java.security.Principal;

public abstract class BaseController {

    protected static final String DEFAULT_SIZE = "20";
    protected static final String DEFAULT_PAGE = "0";

    private static final String DEFAULT_TENANT = "default";

    protected BeanPropertyBindingResult buildErrorObject(final Object object) {

        return new BeanPropertyBindingResult(object, object.getClass().getName());
    }

    protected String getUserTenant(Principal principal) {

        // Will remove comments once spring security is configured
//        final var userProfile = getUserProfile(principal);
//        return userProfile.getTenant();

        return DEFAULT_TENANT;
    }
}
