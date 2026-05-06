package com.proyecto_final.proyecto_final.Repository;
import com.proyecto_final.proyecto_final.Model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    Optional<Ruta> findByNombre(String nombre);
    //List<Ruta> findByCamionId(Long camionId); // Para encontrar rutas asignadas a un camion especifico
}
