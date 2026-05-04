package com.proyecto_final.proyecto_final.Repository;
import com.proyecto_final.proyecto_final.Model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    Servicio findByNombre(String nombre);
}
