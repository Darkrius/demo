package com.example.demo.application.exceptions;

public class ErrorDeConexionExternaException extends RuntimeException {

    public ErrorDeConexionExternaException(String message) {
        super(message);
    }

    public ErrorDeConexionExternaException(String message, Throwable cause) {
        super(message, cause);
    }
}
