package com.example.demo.application.exceptions;

public class ErrorInesperadoException extends RuntimeException {
    public ErrorInesperadoException(String message) {
        super(message);
    }

    public ErrorInesperadoException(String message, Throwable cause) {
        super(message, cause);
    }
}
