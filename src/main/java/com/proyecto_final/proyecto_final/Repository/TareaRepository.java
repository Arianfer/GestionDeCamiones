package com.proyecto_final.proyecto_final.Repository;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import com.proyecto_final.proyecto_final.Model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Todas las tareas de un chofer en los estados indicados.
    // SCRUM-139: se usa también para mi-ruta; la tarea a mostrar se elige en TareaService.
    List<Tarea> findAllByUsuarioIdAndEstadoIn(
            Long usuarioId,
            List<EstadoTarea> estados
    );
}