package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.UsuarioDTO;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listarUsuarios().stream().map(this::toDto).toList();
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtenerPorId(@PathVariable int id) {
        return toDto(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public UsuarioDTO crearUsuario(@RequestBody Usuario usuario) {
        return toDto(usuarioService.crearUsuario(usuario));
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizarUsuario(@PathVariable int id, @RequestBody Usuario usuario) {
        return toDto(usuarioService.actualizarUsuario(id, usuario));
    }

    @PatchMapping("/{id}/desactivar")
    public void desactivarUsuario(@PathVariable int id) {
        usuarioService.desactivarUsuario(id);
    }

    private UsuarioDTO toDto(Usuario usuario) {
        return UsuarioDTO.builder()
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
