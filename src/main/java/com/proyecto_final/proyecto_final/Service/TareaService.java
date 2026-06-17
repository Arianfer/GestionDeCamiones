package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.DTO.Request.TareaRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.RutaResponseDTO;
import com.proyecto_final.proyecto_final.DTO.Response.ServicioResponseDTO;
import com.proyecto_final.proyecto_final.DTO.Response.TareaResponseDTO;
import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import com.proyecto_final.proyecto_final.Model.*;
import com.proyecto_final.proyecto_final.Repository.CamionRepository;
import com.proyecto_final.proyecto_final.Repository.RutaRepository;
import com.proyecto_final.proyecto_final.Repository.TareaRepository;
import com.proyecto_final.proyecto_final.Repository.UsuarioRepository;
import com.proyecto_final.proyecto_final.Excepcion.CamionNoDisponibleException;
import com.proyecto_final.proyecto_final.Excepcion.RutaNoHalladaException;
import com.proyecto_final.proyecto_final.Excepcion.TareaNoEncontradaException;
import com.proyecto_final.proyecto_final.Excepcion.UsuarioDesactivadoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final CamionRepository camionRepository;
    private final RutaRepository rutaRepository;
    private final UsuarioRepository usuarioRepository;


    public TareaResponseDTO crearTarea(TareaRequestDTO dto) {

        //Valido que exista camion, ruta y usuario
        Camion camion = camionRepository.findById(dto.getIdCamion())
                .orElseThrow(() -> new CamionNoDisponibleException("Camión no encontrado"));

        Ruta ruta = rutaRepository.findById(dto.getIdRuta())
                .orElseThrow(() -> new RutaNoHalladaException("Ruta no encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new UsuarioDesactivadoException("Usuario no encontrado"));

        // Valido que el camión esté disponible
        if (camion.getEstadoCamion() != EstadoCamion.DISPONIBLE) {
            throw new CamionNoDisponibleException("El camión no está disponible. Estado actual: "
                    + camion.getEstadoCamion());
        }

        // Cambio estado del camión a EN_RUTA
        camion.setEstadoCamion(EstadoCamion.EN_RUTA);
        camionRepository.save(camion);

        // Creo y guardo tarea
        Tarea tarea = Tarea.builder()
                .descripcion(dto.getDescripcion())
                .fechaEjecucion(dto.getFechaEjecucion())
                .estado(EstadoTarea.PENDIENTE) // siempre arranca PENDIENTE
                .camion(camion)
                .ruta(ruta)
                .usuario(usuario)
                .build();

        return tareaToDto(tareaRepository.save(tarea));
    }

    public List<TareaResponseDTO> listarTareas() {
        return tareaRepository.findAll()
                .stream()
                .map(this::tareaToDto)
                .toList();
    }

    public TareaResponseDTO obtenerPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNoEncontradaException("Tarea no encontrada"));
        return tareaToDto(tarea);
    }

    public TareaResponseDTO actualizarEstado(Long id, EstadoTarea nuevoEstado) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNoEncontradaException("Tarea no encontrada"));

        tarea.setEstado(nuevoEstado);

        // Si se completa o cancela, liberar el camión
        if (nuevoEstado == EstadoTarea.COMPLETADO || nuevoEstado == EstadoTarea.CANCELADO) {
            Camion camion = tarea.getCamion();
            camion.setEstadoCamion(EstadoCamion.DISPONIBLE);
            camionRepository.save(camion);
        }

        return tareaToDto(tareaRepository.save(tarea));
    }

    public RutaResponseDTO obtenerRutaDeChofer(Long choferId) {

        // Busca la tarea PENDIENTE o EN_CURSO del chofer
        Tarea tarea = tareaRepository
                .findByUsuarioIdAndEstadoIn(choferId,
                        List.of(EstadoTarea.PENDIENTE, EstadoTarea.EN_CURSO))
                .orElseThrow(() -> new TareaNoEncontradaException(
                        "No hay tareas activas para el chofer con ID: " + choferId));

        Ruta ruta = tarea.getRuta();

        // Ordenar servicios por campo orden
        List<ServicioResponseDTO> serviciosOrdenados = ruta.getServicios()
                .stream()
                .sorted(Comparator.comparingInt(Servicio::getOrden))
                .map(this::servicioToDto)
                .toList();

        return RutaResponseDTO.builder()
                .idRuta(ruta.getIdRuta())
                .nombre(ruta.getNombre())
                .descripcion(ruta.getDescripcion())
                .servicios(serviciosOrdenados)
                .build();
    }

    // Mapeos a dto
    private TareaResponseDTO tareaToDto(Tarea tarea) {
        return TareaResponseDTO.builder()
                .id(tarea.getId())
                .descripcion(tarea.getDescripcion())
                .fechaEjecucion(tarea.getFechaEjecucion())
                .estado(tarea.getEstado())
                .idCamion(tarea.getCamion().getId())
                .idRuta(tarea.getRuta().getIdRuta())
                .idUsuario(tarea.getUsuario().getId())
                .build();
    }
    private ServicioResponseDTO servicioToDto(Servicio servicio) {
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
                .idRuta(servicio.getRuta().getIdRuta())
                .idCliente(servicio.getCliente().getId())
                .build();
    }
}
