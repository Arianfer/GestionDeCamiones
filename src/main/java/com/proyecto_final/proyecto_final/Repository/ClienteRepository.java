package com.proyecto_final.proyecto_final.Repository;

import com.proyecto_final.proyecto_final.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCuit(String cuit);

    // Métodos para las validaciones de existencia
    boolean existsByCuit(String cuit);
    boolean existsByRazonSocial(String razonSocial);
}