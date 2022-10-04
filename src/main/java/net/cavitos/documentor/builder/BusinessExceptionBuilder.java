package net.cavitos.documentor.builder;

import org.springframework.http.HttpStatus;

import net.cavitos.documentor.domain.exception.BusinessException;

public final class BusinessExceptionBuilder {

    public static BusinessException unprocessableException(final String message, Object ... objects) {

        return new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY, formatString(message, objects));
    }

    public static BusinessException notFoundException(final String message, Object ... objects) {

        return new BusinessException(HttpStatus.NOT_FOUND, formatString(message, objects));
    }
    
    public static BusinessException serverException(final String message, Object ... objects) {

        return new BusinessException(formatString(message, objects));
    }

    // ----------------------------------------------------------------------------------------

    private static String formatString(final String message, Object ... objects) {

        return String.format(message, objects);
    }
}
