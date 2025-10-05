package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when a unique field constraint is violated,
 * such as a duplicate username, email, or post title.
 */
public class DuplicateFieldValidationException extends RuntimeException {

    public DuplicateFieldValidationException(String message) {
        super(message);
    }

}