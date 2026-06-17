package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioLoginRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Request.UsuarioRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.JwtService;
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable int id) {
        return usuarioService.mapeartoDTO(usuarioService.buscarPorId(id));
    }

    @PostMapping
    public UsuarioResponseDTO crearUsuario(@Valid @RequestBody UsuarioRequestDTO dto) {
        return usuarioService.crearUsuario(dto);
    }

    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizarUsuario(@PathVariable int id, @RequestBody UsuarioRequestDTO usuariodto) {
        return usuarioService.actualizarUsuario(id, usuariodto);
    }

    @PatchMapping("/{id}/desactivar")
    public void desactivarUsuario(@PathVariable int id) {
        usuarioService.desactivarUsuario(id);
    }

    @PostMapping("/login")
    public UsuarioResponseDTO login(@RequestBody UsuarioLoginRequestDTO loginRequest) {

        Usuario usuario = usuarioService.autenticar(loginRequest.getDni(), loginRequest.getPassword());

        return usuarioService.mapeartoDTO(usuario);
    }

    // Buscar usuario por DNI
    @GetMapping("/dni/{dni}")
    public UsuarioResponseDTO buscarPorDni(@PathVariable String dni) {

        return usuarioService.mapeartoDTO(usuarioService.buscarPorDni(dni));
    }
}
