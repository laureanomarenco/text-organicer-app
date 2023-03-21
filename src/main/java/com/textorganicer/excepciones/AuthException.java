package com.textorganicer.excepciones;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
