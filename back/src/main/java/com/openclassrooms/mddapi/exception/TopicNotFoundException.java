package com.openclassrooms.mddapi.exception;

/**
 * Exception thrown when a topic with the specified identifier
 * is not found in the system.
 */
public class TopicNotFoundException extends RuntimeException {

    public TopicNotFoundException(String message) {
        super(message);
    }

}