package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
import com.proyecto_final.proyecto_final.Enums.Rol;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Repository.UsuarioRepository;
import com.proyecto_final.proyecto_final.Excepcion.UsuarioDesactivadoException;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import com.proyecto_final.proyecto_final.Excepcion.CredencialesInvalidasException;
import com.proyecto_final.proyecto_final.Excepcion.RecursoDuplicadoException;
import com.proyecto_final.proyecto_final.Excepcion.RecursoNoEncontradoException;
import com.proyecto_final.proyecto_final.Excepcion.ReglaDeNegocioException;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario crearUsuario(UsuarioRequestDTO dto) {
        // Validación de rol
        validarCreacionPorRol(dto.getRol());
        // Validar email duplicado
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con ese email");
        }
        // Validar DNI duplicado
        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con ese DNI");
        }
        // Validar largo del DNI
        if (dto.getDni() == null || dto.getDni().length() < 4) {
            throw new ReglaDeNegocioException("El DNI debe tener al menos 4 números.");
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

    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(this::mapearToDto).toList();
    }

    public Usuario buscarPorId(int id) {
        Usuario usuario = usuarioRepository.findById((long) id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
        // Validacion
        validarPermisoSobreUsuario(usuario);
        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con email: " + email));
    }

    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    // Nuevo metodo para buscar por DNI
    public Usuario buscarPorDni(String dni) {

        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Usuario no encontrado con DNI: " + dni));

        validarPermisoSobreUsuario(usuario);

        return usuario;
    }

    // Login por DNI. Si el DNI no existe o la contraseña no coincide, se responde
    // el mismo mensaje: así no se revela qué DNIs están registrados.
    public Usuario autenticar(String dni, String password) {
        Usuario usuario = usuarioRepository.findByDni(dni)
                .orElseThrow(() -> new CredencialesInvalidasException("DNI o contraseña incorrectos"));

        if (!usuario.getPassword().equals(password)) {
            throw new CredencialesInvalidasException("DNI o contraseña incorrectos");
        }

        if (!usuario.isActivo()) {
            throw new UsuarioDesactivadoException("El usuario se encuentra inactivo en el sistema de Ciageser");
        }

        return usuario;
    }

    // Actualizar datos de un usuario existente
    public Usuario actualizarUsuario(int id, UsuarioRequestDTO dto) {
        Usuario usuarioExistente = buscarPorId(id);

        // OFICINA no puede convertir a un usuario en ADMIN (misma regla que al crear)
        validarCreacionPorRol(dto.getRol());
        // Validar email duplicado en OTRO usuario (el propio puede conservar su email)
        if (usuarioRepository.existsByEmailAndIdNot(dto.getEmail(), usuarioExistente.getId())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con ese email");
        }
        // Validar DNI duplicado en OTRO usuario
        if (usuarioRepository.existsByDniAndIdNot(dto.getDni(), usuarioExistente.getId())) {
            throw new RecursoDuplicadoException("Ya existe un usuario con ese DNI");
        }
        // Validar largo del DNI
        if (dto.getDni() == null || dto.getDni().length() < 4) {
            throw new ReglaDeNegocioException("El DNI debe tener al menos 4 números.");
        }

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

    //MAPEOS
    public UsuarioResponseDTO mapearToDto(Usuario u) {
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

    // Validar que OFICINA no pueda administrar usuarios ADMIN
    private void validarPermisoSobreUsuario(Usuario usuarioObjetivo) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rolActual = auth.getAuthorities().iterator().next().getAuthority();

        if (rolActual.equals("ROLE_OFICINA") && usuarioObjetivo.getRol() == Rol.ADMIN) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No tenés permisos para administrar usuarios ADMIN.");
        }
    }

    // Validar que OFICINA no pueda crear usuarios ADMIN
    private void validarCreacionPorRol(Rol rolNuevoUsuario) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rolActual = auth.getAuthorities().iterator().next().getAuthority();

        if (rolActual.equals("ROLE_OFICINA") && rolNuevoUsuario == Rol.ADMIN) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No tenés permisos para crear usuarios ADMIN.");
        }
    }
}