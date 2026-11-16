package com.proyecto_final.proyecto_final.Excepcion;

import org.springframework.http.HttpStatus;

/**
 * Raíz de todas las excepciones propias de la API.
 * Cada subclase define qué código HTTP le corresponde.
 */
public abstract class ApiException extends RuntimeException {

    private final HttpStatus status;

    protected ApiException(String mensaje, HttpStatus status) {
        super(mensaje);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}