package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Response.ServicioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Servicio;
import com.proyecto_final.proyecto_final.Service.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    @GetMapping
    public List<ServicioResponseDTO> listar() {
        return servicioService.listarServicios().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ServicioResponseDTO obtenerPorId(@PathVariable Long id) {
        return toDto(servicioService.buscarPorId(id));
    }

    @PostMapping
    public ServicioResponseDTO guardar(@RequestBody Servicio servicio) {
        return toDto(servicioService.crearServicio(servicio));
    }

    @PutMapping("/{id}")
    public ServicioResponseDTO actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        return toDto(servicioService.actualizarServicio(id, servicio));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
    }

    // Método helper para mapear Entidad -> DTO de Respuesta
    private ServicioResponseDTO toDto(Servicio servicio) {
        return ServicioResponseDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .prioridad(servicio.getPrioridad())
                .direccion(servicio.getDireccion())
                .latitud(servicio.getLatitud())
                .longitud(servicio.getLongitud())
                .orden(servicio.getOrden())
                .idRuta(servicio.getRuta() != null ? (long) servicio.getRuta().getIdRuta() : null)
                .idCliente(servicio.getCliente() != null ? servicio.getCliente().getId() : null)
                .build();
    }

    @PutMapping("/reordenar")
    public void reordenarServicios(@RequestBody List<Long> idsServiciosOrdenados) {
        servicioService.actualizarOrdenServicios(idsServiciosOrdenados);
    }
}