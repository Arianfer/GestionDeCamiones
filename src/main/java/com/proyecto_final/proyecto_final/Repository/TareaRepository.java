package com.proyecto_final.proyecto_final.Repository;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import com.proyecto_final.proyecto_final.Model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Para buscar la tarea que tiene asignada el chofer actualmente
    Optional<Tarea> findByUsuarioIdAndEstadoIn(Long usuarioId, List<EstadoTarea> estados);

    List<Tarea> findAllByUsuarioIdAndEstadoIn(
            Long usuarioId,
            List<EstadoTarea> estados
    );
}
