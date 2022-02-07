package net.cavitos.documentor.web.controller;

import org.springframework.validation.BeanPropertyBindingResult;

public abstract class BaseController {

    protected static final String DEFAULT_SIZE = "20";
    protected static final String DEFAULT_PAGE = "0";

    protected BeanPropertyBindingResult buildErrorObject(Object object) {

        return new BeanPropertyBindingResult(object, object.getClass().getName());
    }
}
