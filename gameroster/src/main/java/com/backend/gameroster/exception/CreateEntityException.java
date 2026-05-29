package com.backend.gameroster.exception;

public class CreateEntityException extends RuntimeException {

    public <T> CreateEntityException(String nombreClase, T entidad, Throwable cause) {
        super("La entidad " + nombreClase + " no se ha podido crear: " + entidad, cause);
    }
}
