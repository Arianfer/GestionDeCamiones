package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Repository.UsuarioRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        if (usuarioRepository.existsByDni(usuario.getDni())) {
            throw new RuntimeException("Ya existe un usuario con ese DNI");
        }
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(int id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public Usuario actualizarUsuario(int id, Usuario datosNuevos) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNombre(datosNuevos.getNombre());
        usuarioExistente.setApellido(datosNuevos.getApellido());
        usuarioExistente.setEmail(datosNuevos.getEmail());
        usuarioExistente.setDni(datosNuevos.getDni());
        usuarioExistente.setRol(datosNuevos.getRol());

        return usuarioRepository.save(usuarioExistente);
    }

    // Desactivar usuario (baja lógica, no elimina de la base de datos)
    public void desactivarUsuario(int id) {
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}
