package com.proyecto_final.proyecto_final.Repository;
import com.proyecto_final.proyecto_final.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email); //Lo necesitamos para el Login
}
