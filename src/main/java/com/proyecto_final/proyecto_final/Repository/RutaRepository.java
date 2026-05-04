package com.proyecto_final.proyecto_final.Repository;
import com.proyecto_final.proyecto_final.Model.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RutaRepository extends JpaRepository<Ruta, Long> {

    List<Ruta> findByCamionId(Long camionId); // Para encontrar rutas asignadas a un camion especifico
}
