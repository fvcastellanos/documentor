package net.cavitos.documentor.web.controller;

import net.cavitos.documentor.security.domain.UserProfile;
import net.cavitos.documentor.security.service.UserService;
import org.springframework.validation.BeanPropertyBindingResult;

import java.security.Principal;

public abstract class BaseController {

    protected static final String DEFAULT_SIZE = "20";
    protected static final String DEFAULT_PAGE = "0";

    private static final String DEFAULT_TENANT = "default";

    private final UserService userService;

    public BaseController(final UserService userService) {

        this.userService = userService;
    }

    protected BeanPropertyBindingResult buildErrorObject(final Object object) {

        return new BeanPropertyBindingResult(object, object.getClass().getName());
    }

    protected UserProfile getUserProfile(Principal principal) {

        return userService.getUserProfile(principal.getName());
    }

    protected String getUserTenant(Principal principal) {

        final var userProfile = getUserProfile(principal);

        return userProfile.getTenant();
    }
}
