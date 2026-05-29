package com.backend.gameroster.exception;

public class ErrorGenericoException extends RuntimeException {
    public <T> ErrorGenericoException(String error, Throwable cause) {
        super(error, cause);
    }
}
