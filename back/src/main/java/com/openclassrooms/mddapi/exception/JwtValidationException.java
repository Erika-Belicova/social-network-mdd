package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when a JWT token fails validation,
 * such as being expired, malformed, or having an invalid signature.
 */
public class JwtValidationException extends RuntimeException {

    public JwtValidationException(String message) {
        super(message);
    }

}
