package com.proyecto_final.proyecto_final.Excepcion;

import org.springframework.http.HttpStatus;

/** 401: DNI o contraseña incorrectos en el login. */
public class CredencialesInvalidasException extends ApiException {
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje, HttpStatus.UNAUTHORIZED);
    }
}