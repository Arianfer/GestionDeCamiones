package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
import com.proyecto_final.proyecto_final.Enums.Rol;
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

    public Usuario crearUsuario(UsuarioRequestDTO dto) {
        // Validar email duplicado
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        // Validar DNI duplicado
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("Ya existe un usuario con ese DNI");
        }
        // Validar largo del DNI
        if (dto.getDni() == null || dto.getDni().length() < 4) {
            throw new RuntimeException("El DNI debe tener al menos 4 números.");
        }

        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .dni(dto.getDni())
                .email(dto.getEmail())
                .password(dto.getDni().substring(dto.getDni().length() - 4)) // ← últimos 4 dígitos del DNI
                .rol(dto.getRol())
                .activo(true) // ← activo por defecto
                .build();

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

    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    // Nuevo metodo para buscar por DNI
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

    // Actualizar datos de un usuario existente
    public Usuario actualizarUsuario(int id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = buscarPorId(id);

        // Actualizamos solo los campos que vienen en el DTO
        usuarioExistente.setNombre(dto.getNombre());
        usuarioExistente.setApellido(dto.getApellido());
        usuarioExistente.setEmail(dto.getEmail());
        usuarioExistente.setDni(dto.getDni());
        usuarioExistente.setRol(dto.getRol());

        return usuarioRepository.save(usuarioExistente);
    }

    // Desactivar usuario (baja lógica, no elimina de la base de datos)
    public void desactivarUsuario(int id) {
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public UsuarioResponseDTO toDto(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .dni(u.getDni())
                .email(u.getEmail())
                .rol(u.getRol().name())
                .activo(u.isActivo())
                .build();
    }
}