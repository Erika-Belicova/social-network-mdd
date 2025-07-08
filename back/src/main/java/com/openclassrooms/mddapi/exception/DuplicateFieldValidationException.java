package com.openclassrooms.mddapi.exception;

public class DuplicateFieldValidationException extends RuntimeException {

    public DuplicateFieldValidationException(String message) {
        super(message);
    }

}
