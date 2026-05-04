package com.proyecto_final.proyecto_final.Repository;
import com.proyecto_final.proyecto_final.Model.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CamionRepository extends JpaRepository<Camion, Long> {

    Optional<Camion> findByPatente(String patente);
}


