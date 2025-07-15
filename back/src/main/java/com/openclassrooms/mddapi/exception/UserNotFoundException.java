package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when a user with the specified identifier
 * is not found in the system.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

}