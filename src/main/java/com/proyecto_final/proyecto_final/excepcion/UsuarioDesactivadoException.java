package com.proyecto_final.proyecto_final.excepcion;

public class UsuarioDesactivadoException extends RuntimeException {
    public UsuarioDesactivadoException(String message) {
        super(message);
    }
}
