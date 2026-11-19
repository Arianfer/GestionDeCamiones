package com.proyecto_final.proyecto_final.Excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Todas las excepciones propias: cada una trae su código HTTP (404, 409, 401, 400)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> manejarApiException(ApiException ex) {
        return construirRespuesta(ex.getStatus(), ex.getMessage());
    }

    // URL que no existe en la API
    // 403: el usuario está logueado pero no tiene permiso sobre ese dato (SCRUM-138).
    // Sin este handler Spring responde 403 sin cuerpo y el front no puede mostrar el motivo.
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> manejarAccesoDenegado(AccessDeniedException ex) {
        return construirRespuesta(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarUrlInexistente(NoResourceFoundException ex) {
        return construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Falla de @Valid: { campo: mensaje } para mostrar debajo de cada campo del formulario
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // Mismo formato de respuesta para todos los errores: { timestamp, status, error }
    private ResponseEntity<Map<String, Object>> construirRespuesta(HttpStatus status, String mensaje) {
        Map<String, Object> cuerpo = new HashMap<>();
        cuerpo.put("timestamp", LocalDateTime.now());
        cuerpo.put("status", status.value());
        cuerpo.put("error", mensaje);
        return ResponseEntity.status(status).body(cuerpo);
    }
}