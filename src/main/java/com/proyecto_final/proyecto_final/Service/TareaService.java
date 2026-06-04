package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Model.Camion;
import com.proyecto_final.proyecto_final.Model.Tarea;
import com.proyecto_final.proyecto_final.Repository.CamionRepository;
import com.proyecto_final.proyecto_final.Repository.TareaRepository;
import com.proyecto_final.proyecto_final.excepcion.CamionNoDisponibleException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TareaService {

    private final TareaRepository tareaRepository;
    private final CamionService camionService;



    public Tarea asignarTareaDiaria(Tarea nuevaTarea) {
        Camion camion = camionService.buscarPorId(nuevaTarea.getCamion().getId());

        if (camion.getEstadoCamion() != EstadoCamion.DISPONIBLE) {
            throw new CamionNoDisponibleException("El camión con patente " + camion.getPatente() + " no está disponible actualmente (Estado: " + camion.getEstadoCamion() + ")");
        }

        // Si está disponible, se crea la tarea y el camión pasa a EN_RUTA
        camionService.actualizarEstado(camion.getId(), EstadoCamion.EN_RUTA);
        return tareaRepository.save(nuevaTarea);
    }
}
