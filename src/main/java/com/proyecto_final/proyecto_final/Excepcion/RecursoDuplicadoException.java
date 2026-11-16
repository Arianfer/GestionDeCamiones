package com.proyecto_final.proyecto_final.Excepcion;

import org.springframework.http.HttpStatus;

/** 409: ya existe otro registro con ese dato único (DNI, email, CUIT, patente, nombre de ruta). */
public class RecursoDuplicadoException extends ApiException {
    public RecursoDuplicadoException(String mensaje) {
        super(mensaje, HttpStatus.CONFLICT);
    }
}