package com.proyecto_final.proyecto_final.Excepcion;

import org.springframework.http.HttpStatus;

/** 400: el dato tiene formato válido pero rompe una regla del negocio. */
public class ReglaDeNegocioException extends ApiException {
    public ReglaDeNegocioException(String mensaje) {
        super(mensaje, HttpStatus.BAD_REQUEST);
    }
}