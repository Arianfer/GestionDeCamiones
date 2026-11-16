package com.proyecto_final.proyecto_final.Excepcion;

import org.springframework.http.HttpStatus;

/** 404: lo que se busca no existe (servicio, ruta, cliente, camión, usuario, tarea). */
public class RecursoNoEncontradoException extends ApiException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje, HttpStatus.NOT_FOUND);
    }
}