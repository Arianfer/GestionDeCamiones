package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
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

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO usuariodto) {

        if (usuarioRepository.existsByEmail(usuariodto.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con ese email");
        }
        if (usuarioRepository.existsByDni(usuariodto.getDni())) {
            throw new RuntimeException("Ya existe un usuario con ese DNI");
        }

        // --- ASIGNACIÓN DE CONTRASEÑA AUTOMÁTICA ---
        if (usuariodto.getDni() == null || usuariodto.getDni().length() < 4) {
            throw new RuntimeException("El DNI debe tener al menos 4 números para generar la contraseña.");
        }
        // Le asigna como password los últimos 4 dígitos del DNI ingresado
        usuariodto.setPassword(usuariodto.getDni().substring(usuariodto.getDni().length() - 4));

        Usuario usuario = mapeartoEntidad(usuariodto);
        // Lo activamos por defecto al crearlo
        usuario.setActivo(true);

        return mapeartoDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(this::mapeartoDTO).toList();
    }

    public Usuario buscarPorId(int id) {
        return  usuarioRepository.findById((long) id)
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

    public UsuarioResponseDTO actualizarUsuario(int id, UsuarioRequestDTO datosNuevos) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNombre(datosNuevos.getNombre());
        usuarioExistente.setApellido(datosNuevos.getApellido());
        usuarioExistente.setEmail(datosNuevos.getEmail());
        usuarioExistente.setDni(datosNuevos.getDni());
        usuarioExistente.setRol(datosNuevos.getRol());

        return mapeartoDTO(usuarioRepository.save(usuarioExistente));
    }

    // Desactivar usuario (baja lógica, no elimina de la base de datos)
    public void desactivarUsuario(int id) {
        Usuario usuario = buscarPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    public Usuario mapeartoEntidad(UsuarioRequestDTO dto) {
        return Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .dni(dto.getDni())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol())
                .build();
    }

    public UsuarioResponseDTO mapeartoDTO(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .dni(usuario.getDni())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .build();
    }
}