package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Model.Camion;
import com.proyecto_final.proyecto_final.Repository.CamionRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CamionService {

    private final CamionRepository camionRepository;

    // Crear camion
    public Camion crearCamion(Camion camion) {
        if (camionRepository.findByPatente(camion.getPatente()).isPresent()) {
            throw new RuntimeException("Ya existe un camion con la patente: " + camion.getPatente());
        }
        return camionRepository.save(camion);
    }

    // Listar todos
    public List<Camion> listarCamiones() {
        return camionRepository.findAll();
    }

    // Buscar por ID
    public Camion buscarPorId(Long id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con id: " + id));
    }

    // Buscar por patente
    public Camion buscarPorPatente(String patente) {
        return camionRepository.findByPatente(patente)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con patente: " + patente));
    }

    // Actualizar estado del camion
    public Camion actualizarEstado(Long id, EstadoCamion nuevoEstado) {
        Camion camion = buscarPorId(id);
        camion.setEstadoCamion(nuevoEstado);
        return camionRepository.save(camion);
    }

    // Actualizar camion completo
    public Camion actualizarCamion(Long id, Camion datosNuevos) {
        Camion camionExistente = buscarPorId(id);

        camionExistente.setPatente(datosNuevos.getPatente());
        camionExistente.setTipo(datosNuevos.getTipo());
        camionExistente.setEstadoCamion(datosNuevos.getEstadoCamion());
        camionExistente.setCapacidadCarga(datosNuevos.getCapacidadCarga());
        camionExistente.setConsumoDieselPorKm(datosNuevos.getConsumoDieselPorKm());

        return camionRepository.save(camionExistente);
    }
    //Eliminar camion
    public void eliminarCamion(Long id) {
        buscarPorId(id); // verifica que existe antes de eliminar
        camionRepository.deleteById(id);
    }
}
