package com.proyecto_final.proyecto_final.Excepcion;

public class UsuarioDesactivadoException extends ReglaDeNegocioException {
    public UsuarioDesactivadoException(String message) {
        super(message);
    }
}
