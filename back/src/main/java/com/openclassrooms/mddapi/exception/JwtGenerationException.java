package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when an error occurs during the generation of a JWT token,
 * typically due to invalid claims or signing issues.
 */
public class JwtGenerationException extends RuntimeException {

    public JwtGenerationException(String message) {
        super(message);
    }

}