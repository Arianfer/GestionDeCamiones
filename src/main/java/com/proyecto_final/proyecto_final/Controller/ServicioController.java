package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.ServicioRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.ServicioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Cliente;
import com.proyecto_final.proyecto_final.Model.Ruta;
import com.proyecto_final.proyecto_final.Model.Servicio;
import com.proyecto_final.proyecto_final.Service.ServicioService;
import jakarta.validation.Valid;
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
                .map(this::toResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ServicioResponseDTO obtenerPorId(@PathVariable Long id) {
        return toResponseDto(servicioService.buscarPorId(id));
    }

    @PostMapping
    public ServicioResponseDTO guardar(@Valid @RequestBody ServicioRequestDTO request) {
        Servicio servicio = toEntity(request);
        return toResponseDto(servicioService.crearServicio(servicio));
    }

    @PutMapping("/{id}")
    public ServicioResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody ServicioRequestDTO request) {
        Servicio servicio = toEntity(request);
        return toResponseDto(servicioService.actualizarServicio(id, servicio));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        servicioService.eliminarServicio(id);
    }

    @PutMapping("/reordenar")
    public void reordenarServicios(@RequestBody List<Long> idsServiciosOrdenados) {
        servicioService.actualizarOrdenServicios(idsServiciosOrdenados);
    }

    // Convertir DTO de Entrada -> Entidad Servicio
    private Servicio toEntity(ServicioRequestDTO dto) {
        Ruta ruta = null;
        if (dto.getIdRuta() != null) {
            ruta = new Ruta();
            ruta.setIdRuta(dto.getIdRuta());
        }

        Cliente cliente = null;
        if (dto.getIdCliente() != null) {
            cliente = new Cliente();
            cliente.setId(dto.getIdCliente());
        }

        return Servicio.builder()
                .nombre(dto.getNombre())
                .prioridad(dto.getPrioridad())
                .direccion(dto.getDireccion())
                .tipoResiduo(dto.getTipoResiduo())
                .frecuencia(dto.getFrecuencia())
                .latitud(dto.getLatitud())
                .longitud(dto.getLongitud())
                .orden(dto.getOrden())
                .ruta(ruta)
                .cliente(cliente)
                .build();
    }

    // Convertir Entidad -> DTO de Respuesta
    private ServicioResponseDTO toResponseDto(Servicio servicio) {
        return ServicioResponseDTO.builder()
                .id(servicio.getId())
                .nombre(servicio.getNombre())
                .prioridad(servicio.getPrioridad())
                .direccion(servicio.getDireccion())
                .tipoResiduo(servicio.getTipoResiduo())
                .frecuencia(servicio.getFrecuencia())
                .latitud(servicio.getLatitud())
                .longitud(servicio.getLongitud())
                .orden(servicio.getOrden())
                .idRuta(servicio.getRuta() != null ? servicio.getRuta().getIdRuta() : null)
                .idCliente(servicio.getCliente() != null ? servicio.getCliente().getId() : null)
                .build();
    }
}