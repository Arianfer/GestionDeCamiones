package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.ServicioDTO;
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
    public List<ServicioDTO> listar() {
        return servicioService.listarServicios().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ServicioDTO obtenerPorId(@PathVariable Long id) {
        return toDto(servicioService.buscarPorId(id));
    }

    @PostMapping
    public ServicioDTO guardar(@RequestBody Servicio servicio) {
        return toDto(servicioService.crearServicio(servicio));
    }

    @PutMapping("/{id}")
    public ServicioDTO actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        return toDto(servicioService.actualizarServicio(id, servicio));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
    }

    // Método helper para mapear Entidad -> DTO siguiendo tu estilo
    private ServicioDTO toDto(Servicio servicio) {
        return ServicioDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .prioridad(servicio.getPrioridad().ordinal()) // o .name() si usás String en el DTO
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