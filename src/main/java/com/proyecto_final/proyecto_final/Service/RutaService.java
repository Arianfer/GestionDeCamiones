package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Model.Ruta;
import com.proyecto_final.proyecto_final.Repository.RutaRepository;
import com.proyecto_final.proyecto_final.excepcion.RutaNoHalladaException;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RutaService {

    private final RutaRepository rutaRepository;

    // Crear ruta
    public Ruta crearRuta(Ruta ruta) {
        if (rutaRepository.findByNombre(ruta.getNombre()).isPresent()) {
            throw new RuntimeException("Ya existe una ruta con el nombre: " + ruta.getNombre());
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

        rutaExistente.setNombre(datosNuevos.getNombre());
        rutaExistente.setDescripcion(datosNuevos.getDescripcion());

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
