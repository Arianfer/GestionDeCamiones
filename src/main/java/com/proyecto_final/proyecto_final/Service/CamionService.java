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

    public Camion crearCamion(Camion camion) {
        if (camionRepository.findByPatente(camion.getPatente()).isPresent()) {
            throw new PatenteInvalidaException("Ya existe un camión registrado con la patente: " + camion.getPatente());
        }
        return camionRepository.save(camion);
    }

    public List<Camion> listarCamiones() {
        return camionRepository.findAll();
    }

    public Camion buscarPorId(Long id) {
        return camionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con id: " + id));
    }

    public Camion buscarPorPatente(String patente) {
        return camionRepository.findByPatente(patente)
                .orElseThrow(() -> new RuntimeException("Camion no encontrado con patente: " + patente));
    }

    public Camion actualizarEstado(Long id, EstadoCamion nuevoEstado) {
        Camion camion = buscarPorId(id);
        camion.setEstadoCamion(nuevoEstado);
        return camionRepository.save(camion);
    }

    public Camion actualizarCamion(Long id, Camion datosNuevos) {
        Camion camionExistente = buscarPorId(id);

        camionExistente.setPatente(datosNuevos.getPatente());
        camionExistente.setTipo(datosNuevos.getTipo());
        camionExistente.setEstadoCamion(datosNuevos.getEstadoCamion());
        camionExistente.setCapacidadCarga(datosNuevos.getCapacidadCarga());
        camionExistente.setConsumoDieselPorKm(datosNuevos.getConsumoDieselPorKm());

        return camionRepository.save(camionExistente);
    }

    public void eliminarCamion(Long id) {
        buscarPorId(id); // verifica que existe antes de eliminar
        camionRepository.deleteById(id);
    }

    /// Agregados que los necesitaba para el controller. Arian)
    public Camion guardarCamion(Camion camion) {
        return camionRepository.save(camion);
    }

    public Double calcularCostoViaje(Long id, Double km) {
        // Buscamos el camión por ID usando el repository si no existe, lanzamos una excepción
        Camion camion = camionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Camión no encontrado con el ID: " + id));

        // Definimos el precio del Diesel. Ej el precio promedio actual en estaciones de servicio
        Double precioDiesel = 1100.0;

        // Obtenemos el consumo específico del modelo (ej: 0.35 litros por km)
        Double consumoPorKm = camion.getConsumoDieselPorKm();

        // Verificamos para no hacer calculos erroneos y sino devolvemos el calculo
        if (consumoPorKm == null || consumoPorKm <= 0) {
            return 0.0;
        }

        return (km * consumoPorKm) * precioDiesel;
    }
}
