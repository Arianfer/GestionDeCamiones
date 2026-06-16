package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Repository.UsuarioRepository;
import com.proyecto_final.proyecto_final.Excepcion.UsuarioDesactivadoException;
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

        // --- ASIGNACIÓN DE CONTRASEÑA AUTOMÁTICA ---
        if (usuario.getDni() == null || usuario.getDni().length() < 4) {
            throw new RuntimeException("El DNI debe tener al menos 4 números para generar la contraseña.");
        }
        // Le asigna como password los últimos 4 dígitos del DNI ingresado
        usuario.setPassword(usuario.getDni().substring(usuario.getDni().length() - 4));

        // Lo activamos por defecto al crearlo
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(int id) {
        return usuarioRepository.findById((long) id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    // Nuevo método para buscar por DNI
    public Usuario buscarPorDni(String dni) {
        return usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con DNI: " + dni));
    }

    // Autenticación cambiada para que pida el DNI en vez del Email
    public Usuario autenticar(String dni, String password) {
        Usuario usuario = buscarPorDni(dni);

        if (!usuario.isActivo()) {
            throw new UsuarioDesactivadoException("El usuario se encuentra inactivo en el sistema de Ciageser");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Credenciales invalidas");
        }

        return usuario;
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