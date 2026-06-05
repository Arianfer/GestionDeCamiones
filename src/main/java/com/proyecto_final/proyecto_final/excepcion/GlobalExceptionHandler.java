package com.proyecto_final.proyecto_final.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejador para tus excepciones de negocio (Bad Request - 400)
    @ExceptionHandler({
            CamionNoDisponibleException.class,
            PatenteInvalidaException.class,
            RutaNoHalladaException.class,
            UsuarioDesactivadoException.class
    })
    public ResponseEntity<Map<String, Object>> manejarExcepcionesDeNegocio(RuntimeException ex) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("error", ex.getMessage());
        cuerpo.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(cuerpo, HttpStatus.BAD_REQUEST);
    }

    // 2. Manejador secundario por si se escapa cualquier otro error genérico (Internal Server Error - 500)
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(
            NoResourceFoundException ex) {

        Map<String, Object> cuerpo = new HashMap<>();

        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("error", ex.getMessage());
        cuerpo.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(cuerpo, HttpStatus.NOT_FOUND);
    }
}