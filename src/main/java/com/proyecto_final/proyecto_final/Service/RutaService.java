package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Model.Ruta;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Repository.RutaRepository;
import com.proyecto_final.proyecto_final.excepcion.RutaNoHalladaException;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;
    // Inyectamos el servicio de usuarios para poder validar al chofer
    private final UsuarioService usuarioService;

    // Crear ruta
    public Ruta crearRuta(Ruta ruta) {
        // 1. Mantenemos tu validación original de nombre duplicado
        if (rutaRepository.findByNombre(ruta.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe una ruta con el nombre: " + ruta.getNombre());
        }

        // 2. Validar que venga el ID de un chofer desde el DTO
        if (ruta.getChofer() == null || ruta.getChofer().getId() == null) {
            throw new RuntimeException("La ruta debe tener un empleado asignado obligatoriamente.");
        }

        // 3. Buscar al empleado y validar que su rol sea el correcto
        Usuario chofer = usuarioService.buscarPorId(ruta.getChofer().getId().intValue());
        if (!chofer.getRol().name().equals("EMPLEADO")) {
            throw new RuntimeException("Error: El usuario asignado a la ruta no es un Chofer (EMPLEADO).");
        }
        ruta.setChofer(chofer);

        // 4. Manejo inteligente de fecha: si la oficina no mandó fecha, asignamos la de hoy
        if (ruta.getFecha() == null) {
            ruta.setFecha(LocalDate.now());
        }

        return rutaRepository.save(ruta);
    }

    // Listar todas
    public List<Ruta> listarRutas() {
        return rutaRepository.findAll();
    }

    // Buscar por ID
    public Ruta buscarPorId(Long id) {
        return rutaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con id: " + id));
    }

    // Buscar por nombre
    public Ruta buscarPorNombre(String nombre) {
        return rutaRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Ruta no encontrada con nombre: " + nombre));
    }

    // Actualizar ruta
    public Ruta actualizarRuta(Long id, Ruta datosNuevos) {
        Ruta rutaExistente = buscarPorId(id);

        // Mantenemos tus actualizaciones originales
        rutaExistente.setNombre(datosNuevos.getNombre());
        rutaExistente.setDescripcion(datosNuevos.getDescripcion());

        // Si desde el front mandan una fecha nueva, la pisamos
        if (datosNuevos.getFecha() != null) {
            rutaExistente.setFecha(datosNuevos.getFecha());
        }

        // Si mandan un chofer nuevo, lo validamos de vuelta antes de cambiarlo
        if (datosNuevos.getChofer() != null && datosNuevos.getChofer().getId() != null) {
            Usuario nuevoChofer = usuarioService.buscarPorId(datosNuevos.getChofer().getId().intValue());
            if (!nuevoChofer.getRol().name().equals("EMPLEADO")) {
                throw new RuntimeException("Error: El usuario asignado no es un Chofer (EMPLEADO).");
            }
            rutaExistente.setChofer(nuevoChofer);
        }

        return rutaRepository.save(rutaExistente);
    }

    // Eliminar ruta
    public void eliminarRuta(Long id) {
        buscarPorId(id); // verifica que existe antes de eliminar
        rutaRepository.deleteById(id);
    }

    public Ruta buscarRutaParaChofer(Long id) {
        Ruta ruta = buscarPorId(id);
        if (ruta.getServicios() == null || ruta.getServicios().isEmpty()) {
            // LANZAR ACÁ:
            throw new RutaNoHalladaException("La ruta '" + ruta.getNombre() + "' no tiene puntos de recolección asignados todavía.");
        }
        return ruta;
    }
}