package com.proyecto_final.proyecto_final.Repository;
import com.proyecto_final.proyecto_final.Enums.Rol;
import com.proyecto_final.proyecto_final.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
    Optional<Usuario> findByEmail(String email); //Lo necesitamos para el Login
    Optional<Usuario> findByDni(String dni);
    List<Usuario> findByRol(Rol rol);
}
