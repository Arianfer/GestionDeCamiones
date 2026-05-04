package com.proyecto_final.proyecto_final.Repository;
import com.proyecto_final.proyecto_final.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    Optional<Usuario> findByEmail(String email); //Lo necesitamos para el Login
}
