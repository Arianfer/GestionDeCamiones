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
import com.proyecto_final.proyecto_final.Enums.Rol;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import com.proyecto_final.proyecto_final.Excepcion.RecursoNoEncontradoException;
import com.proyecto_final.proyecto_final.Excepcion.ReglaDeNegocioException;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final CamionRepository camionRepository;
    private final RutaRepository rutaRepository;
    private final UsuarioRepository usuarioRepository;


    public TareaResponseDTO crearTarea(TareaRequestDTO dto) {

        // Reviso que todas las entidades existan
        Camion camion = camionRepository.findById(dto.getIdCamion())
                .orElseThrow(() -> new RecursoNoEncontradoException("Camión no encontrado"));

        Ruta ruta = rutaRepository.findById(dto.getIdRuta())
                .orElseThrow(() -> new RutaNoHalladaException("Ruta no encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        // SCRUM-141: solo se le asignan recorridos a choferes activos.
        // Va antes de tocar el camión, así un error no lo deja marcado EN_RUTA.
        validarChoferAsignable(usuario);

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

    // LISTADO TAREAS
    public List<TareaResponseDTO> listarTareas() {
        return tareaRepository.findAll()
                .stream()
                .map(this::tareaToDto)
                .toList();
    }

    // Tarea por ID
    public TareaResponseDTO obtenerPorId(Long id) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNoEncontradaException("Tarea no encontrada"));
        return tareaToDto(tarea);
    }

    // Tareas del Chofer (incluye COMPLETADO para que nunca desaparezcan de su semana)
    public List<TareaResponseDTO> obtenerTareasActivasChofer(Long choferId) {
        // Un chofer solo puede ver sus propias tareas
        validarAccesoAChofer(choferId);

        return tareaRepository
                .findAllByUsuarioIdAndEstadoIn(
                        choferId,
                        List.of(
                                EstadoTarea.PENDIENTE,
                                EstadoTarea.EN_CURSO,
                                EstadoTarea.COMPLETADO
                        )
                )
                .stream()
                .map(this::tareaToDto)
                .toList();
    }

    // Actualizar
    public TareaResponseDTO actualizarEstado(Long id, EstadoTarea nuevoEstado) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new TareaNoEncontradaException("Tarea no encontrada"));

        // Un chofer solo puede cambiar el estado de SUS tareas
        validarAccesoAChofer(tarea.getUsuario().getId());

        tarea.setEstado(nuevoEstado);

        // Si se completa o cancela, liberar el camión
        if (nuevoEstado == EstadoTarea.COMPLETADO || nuevoEstado == EstadoTarea.CANCELADO) {
            Camion camion = tarea.getCamion();
            camion.setEstadoCamion(EstadoCamion.DISPONIBLE);
            camionRepository.save(camion);
        }

        return tareaToDto(tareaRepository.save(tarea));
    }

    // ── Endpoint estrella: ruta del chofer ────────────────────────────────────
    public RutaResponseDTO obtenerRutaDeChofer(Long choferId) {
        // Un chofer solo puede ver su propia ruta
        validarAccesoAChofer(choferId);

        // SCRUM-139: el chofer puede tener varias tareas (toda la semana).
        // Antes se pedía una sola y, si había más de una, el endpoint daba 500.
        List<Tarea> tareas = tareaRepository.findAllByUsuarioIdAndEstadoIn(
                choferId,
                List.of(EstadoTarea.PENDIENTE, EstadoTarea.EN_CURSO, EstadoTarea.COMPLETADO));

        Tarea tarea = elegirTareaParaMostrar(tareas)
                .orElseThrow(() -> new TareaNoEncontradaException(
                        "No hay tareas para el chofer con ID: " + choferId));

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

    // SCRUM-139: de todas las tareas del chofer, elige cuál mostrar en mi-ruta.
    // 1) La que está EN_CURSO (si arrancó un recorrido, ese es el que importa).
    // 2) Si no, la de hoy o la próxima (la de fecha más cercana desde hoy en adelante).
    // 3) Si todas son de días anteriores, la más reciente.
    private Optional<Tarea> elegirTareaParaMostrar(List<Tarea> tareas) {
        List<Tarea> conFecha = tareas.stream()
                .filter(t -> t.getFechaEjecucion() != null)
                .toList();

        Optional<Tarea> enCurso = conFecha.stream()
                .filter(t -> t.getEstado() == EstadoTarea.EN_CURSO)
                .min(Comparator.comparing(Tarea::getFechaEjecucion));
        if (enCurso.isPresent()) {
            return enCurso;
        }

        LocalDate hoy = LocalDate.now();
        Optional<Tarea> deHoyEnAdelante = conFecha.stream()
                .filter(t -> !t.getFechaEjecucion().toLocalDate().isBefore(hoy))
                .min(Comparator.comparing(Tarea::getFechaEjecucion));
        if (deHoyEnAdelante.isPresent()) {
            return deHoyEnAdelante;
        }

        return conFecha.stream()
                .max(Comparator.comparing(Tarea::getFechaEjecucion));
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

    // SCRUM-141: el usuario de una tarea tiene que ser un chofer (EMPLEADO) y estar activo.
    private void validarChoferAsignable(Usuario usuario) {
        if (usuario.getRol() != Rol.EMPLEADO) {
            throw new ReglaDeNegocioException(
                    "Solo se pueden asignar tareas a choferes (rol EMPLEADO). El usuario elegido es "
                            + usuario.getRol() + ".");
        }
        if (!usuario.isActivo()) {
            throw new ReglaDeNegocioException(
                    "El chofer está desactivado y no se le pueden asignar tareas.");
        }
    }

    // SCRUM-138: un EMPLEADO (chofer) solo puede acceder a sus propias tareas.
    // ADMIN y OFICINA pueden acceder a las de cualquier chofer.
    private void validarAccesoAChofer(Long choferId) {
        Usuario usuarioActual = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (usuarioActual.getRol() == Rol.EMPLEADO && !usuarioActual.getId().equals(choferId)) {
            throw new AccessDeniedException("No tenés permiso para acceder a las tareas de otro chofer.");
        }
    }
}