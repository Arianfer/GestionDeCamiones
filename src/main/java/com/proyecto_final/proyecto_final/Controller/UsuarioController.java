package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioLoginRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Request.UsuarioRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
import com.proyecto_final.proyecto_final.Enums.Rol;
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Listar todos los usuarios del sistema (OFICINA solo ve choferes, ADMIN ve todos)
    @GetMapping
    public List<UsuarioResponseDTO> listar(Authentication authentication) {
        String rol = authentication.getAuthorities().iterator().next().getAuthority();

        if (rol.equals("ROLE_OFICINA")) {
            // Solo devuelve choferes
            return usuarioService.listarPorRol(Rol.EMPLEADO).stream()
                    .map(usuarioService::mapearToDto)
                    .toList();
        }
        // ADMIN ve todos
        return usuarioService.listarUsuarios();
    }

    // Buscar usuario por ID
    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable int id) {
        return usuarioService.mapearToDto(usuarioService.buscarPorId(id));
    }

    // Buscar usuario por DNI
    @GetMapping("/dni/{dni}")
    public UsuarioResponseDTO buscarPorDni(@PathVariable String dni) {
        return usuarioService.mapearToDto(usuarioService.buscarPorDni(dni));
    }

    // Crear nuevo usuario - la password se genera automáticamente con los últimos 4 dígitos del DNI
    @PostMapping
    public UsuarioResponseDTO crearUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.mapearToDto(usuarioService.crearUsuario(dto));
    }

    // Modificar datos de un usuario existente
    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizarUsuario(@PathVariable int id,
                                                @Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.mapearToDto(usuarioService.actualizarUsuario(id, dto));
    }

    // Desactivar usuario - baja lógica, no elimina de la base de datos
    @PatchMapping("/{id}/desactivar")
    public void desactivarUsuario(@PathVariable int id) {
        usuarioService.desactivarUsuario(id);
    }

    // Login con DNI y password
    @PostMapping("/login")
    public UsuarioResponseDTO login(@RequestBody UsuarioLoginRequestDTO loginRequest) {
        return usuarioService.mapearToDto(
                usuarioService.autenticar(loginRequest.getDni(), loginRequest.getPassword()));
    }
}