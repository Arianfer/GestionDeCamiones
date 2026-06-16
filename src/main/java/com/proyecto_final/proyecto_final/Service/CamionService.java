package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Model.Camion;
import com.proyecto_final.proyecto_final.Repository.CamionRepository;
import com.proyecto_final.proyecto_final.Excepcion.PatenteInvalidaException;
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
            throw new PatenteInvalidaException("Ya existe un camión registrado con la patente: " + camion.getPatente());
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

    /// Agregados que los necesitaba para el controller. Arian)
    public Camion guardarCamion(Camion camion) {
        return camionRepository.save(camion);
    }

    public Double calcularCostoViaje(Long id, Double km) {
        // 1. Buscamos el camión por ID usando el Repository
        // Si no existe, lanzamos una excepción (podes personalizar el mensaje)
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado con el ID: " + id));

        // 2. Definimos el precio del Diesel (Podrías tener esto en un archivo de configuración)
        // Precio promedio actual en estaciones de servicio locales
        Double precioDiesel = 1100.0;

        // 3. Obtenemos el consumo específico del modelo (ej: 0.35 litros por km)
        Double consumoPorKm = camion.getConsumoDieselPorKm();

        // 4. Lógica de negocio: (Km * Consumo) * Precio
        if (consumoPorKm == null || consumoPorKm <= 0) {
            return 0.0; // Evitamos cálculos erróneos si el dato no está cargado
        }


        return (km * consumoPorKm) * precioDiesel;
    }
}
