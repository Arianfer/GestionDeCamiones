package com.proyecto_final.proyecto_final.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import com.proyecto_final.proyecto_final.Excepcion.CamionNoDisponibleException;
import com.proyecto_final.proyecto_final.Excepcion.PatenteInvalidaException;
import com.proyecto_final.proyecto_final.Excepcion.RutaNoHalladaException;
import com.proyecto_final.proyecto_final.Excepcion.UsuarioDesactivadoException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Manejador de nuestras excepciones de negocio, (Bad Request - 400)
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

    // Manejador secundario por si se escapa cualquier otro error genérico, ej: Internal Server Error - 500
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(
            NoResourceFoundException ex) {

        Map<String, Object> cuerpo = new HashMap<>();

        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("error", ex.getMessage());
        cuerpo.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(cuerpo, HttpStatus.NOT_FOUND);
    }
    //Manejador de validaciones al cargar, que da respuestas especificas
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(
            MethodArgumentNotValidException ex) {

        Map<String, String> errores = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errores.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
}