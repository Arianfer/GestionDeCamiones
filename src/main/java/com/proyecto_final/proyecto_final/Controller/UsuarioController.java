package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioLoginRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listarUsuarios().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable int id) {
        return toDto(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public UsuarioResponseDTO crearUsuario(@RequestBody Usuario usuario) {
        return toDto(usuarioService.crearUsuario(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        return toDto(usuarioService.actualizarUsuario(id, usuario));
    }

    @PatchMapping("/{id}/desactivar")
    public void desactivarUsuario(@PathVariable int id) {
        usuarioService.desactivarUsuario(id);
    }

    private UsuarioResponseDTO toDto(Usuario usuario) {
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

    @PostMapping("/login")
    public UsuarioResponseDTO login(@RequestBody UsuarioLoginRequestDTO loginRequest) {
        // Ahora le pasamos el DNI en vez del Email
        Usuario usuario = usuarioService.autenticar(loginRequest.getDni(), loginRequest.getPassword());
        return toDto(usuario);
    }

    // Buscar usuario por DNI
    @GetMapping("/dni/{dni}")
    public UsuarioResponseDTO buscarPorDni(@PathVariable String dni) {
        Usuario usuario = usuarioService.buscarPorDni(dni);
        return toDto(usuario); // Asumiendo que tenés un método toDto acá también
    }
}
