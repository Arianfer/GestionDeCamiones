package com.proyecto_final.proyecto_final.Controller;


import com.proyecto_final.proyecto_final.DTO.Request.TareaRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.RutaResponseDTO;
import com.proyecto_final.proyecto_final.DTO.Response.TareaResponseDTO;
import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import com.proyecto_final.proyecto_final.Service.TareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<TareaResponseDTO>> listarTareas() {
        return ResponseEntity.ok(tareaService.listarTareas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tareaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<TareaResponseDTO> crearTarea(@RequestBody TareaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crearTarea(dto));
    }

    // Actualizar estado, puede ser chofer u oficina
    @PatchMapping("/{id}/estado")
    public ResponseEntity<TareaResponseDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoTarea nuevoEstado) {
        return ResponseEntity.ok(tareaService.actualizarEstado(id, nuevoEstado));
    }

    // Endpoint estrella: ruta del chofer (app mobile)
    @GetMapping("/mi-ruta/{choferId}")
    public ResponseEntity<RutaResponseDTO> obtenerRutaDeChofer(
            @PathVariable Long choferId) {
        return ResponseEntity.ok(tareaService.obtenerRutaDeChofer(choferId));
    }
}
