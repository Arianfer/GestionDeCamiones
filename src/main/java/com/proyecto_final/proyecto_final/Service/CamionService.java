package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.DTO.Request.CamionRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.CamionResponseDTO;
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

    public CamionResponseDTO crearCamion(CamionRequestDTO camionDto) {

        if (camionRepository.findByPatente(camionDto.getPatente()).isPresent()) {
            throw new PatenteInvalidaException("Ya existe un camión registrado con la patente: " + camionDto.getPatente());
        }

        Camion camion = mapearToEntidad(camionDto);

        return mapearToDto(camionRepository.save(camion));
    }

    public List<CamionResponseDTO> listarCamiones() {
        return camionRepository.findAll().stream().map(this::mapearToDto).toList();
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

    public CamionResponseDTO actualizarCamion(Long id, CamionRequestDTO datosNuevos) {
        Camion camionExistente = buscarPorId(id);

        camionExistente.setPatente(datosNuevos.getPatente());
        camionExistente.setTipo(datosNuevos.getTipo());
        camionExistente.setEstadoCamion(datosNuevos.getEstadoCamion());
        camionExistente.setCapacidadCarga(datosNuevos.getCapacidadCarga());
        camionExistente.setConsumoDieselPorKm(datosNuevos.getConsumoDieselPorKm());

        return mapearToDto(camionRepository.save(camionExistente));
    }

    public void eliminarCamion(Long id) {
        buscarPorId(id); // verifica que existe antes de eliminar
        camionRepository.deleteById(id);
    }

    /// Agregados que los necesitaba para el controller. Arian)

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

    //MAPEOS
    private CamionResponseDTO mapearToDto(Camion camion) {
        return CamionResponseDTO.builder()
                .id(camion.getId())
                .patente(camion.getPatente())
                .tipo(camion.getTipo().name())
                .estado(camion.getEstadoCamion())
                .capacidadCarga(camion.getCapacidadCarga())
                .consumoDieselPorKm(camion.getConsumoDieselPorKm())
                .build();
    }
    private Camion mapearToEntidad(CamionRequestDTO dto) {
        return Camion.builder()
                .patente(dto.getPatente())
                .tipo(dto.getTipo())
                .estadoCamion(dto.getEstadoCamion())
                .capacidadCarga(dto.getCapacidadCarga())
                .consumoDieselPorKm(dto.getConsumoDieselPorKm())
                .build();
    }
}
