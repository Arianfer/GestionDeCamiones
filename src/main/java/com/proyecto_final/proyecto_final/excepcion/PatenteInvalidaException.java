package com.proyecto_final.proyecto_final.excepcion;

public class PatenteInvalidaException extends RuntimeException {
    public PatenteInvalidaException(String message) {
        super(message);
    }
}
