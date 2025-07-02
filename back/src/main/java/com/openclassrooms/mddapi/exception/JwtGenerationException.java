package com.openclassrooms.mddapi.exception;

public class JwtGenerationException extends RuntimeException {

    public JwtGenerationException(String message) {
        super(message);
    }

}