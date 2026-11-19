package com.proyecto_final.proyecto_final.Repository;

import com.proyecto_final.proyecto_final.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCuit(String cuit);

    // Métodos para las validaciones de existencia
    boolean existsByCuit(String cuit);
    boolean existsByRazonSocial(String razonSocial);
    // Para validar duplicados al editar: busca otro cliente (id distinto) con el mismo dato
    boolean existsByCuitAndIdNot(String cuit, Long id);
    boolean existsByRazonSocialAndIdNot(String razonSocial, Long id);
}