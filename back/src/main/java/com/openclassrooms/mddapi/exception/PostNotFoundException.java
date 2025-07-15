package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when a post with the specified identifier
 * is not found in the system.
 */
public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(String message) {
        super(message);
    }

}