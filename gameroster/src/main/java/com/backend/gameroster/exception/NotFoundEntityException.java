package com.backend.gameroster.exception;

public class NotFoundEntityException extends RuntimeException {
    public <T> NotFoundEntityException(String mensaje) {
        super(mensaje);
    }
}
