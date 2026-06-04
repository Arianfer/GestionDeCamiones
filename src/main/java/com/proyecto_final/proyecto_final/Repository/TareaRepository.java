package com.proyecto_final.proyecto_final.Repository;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import com.proyecto_final.proyecto_final.Model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    // Para buscar la tarea que tiene asignada el chofer actualmente
    List<Tarea> findByUsuarioIdAndEstado(Long usuarioId, EstadoTarea estado);
}
