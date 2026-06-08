package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioLoginRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    // EL ÚNICO ENDPOINT PÚBLICO
    @PostMapping("/login")
    public UsuarioResponseDTO login(@RequestBody UsuarioLoginRequestDTO loginRequest) {
        // El servicio verifica si el usuario existe, si está activo y si la pass coincide
        Usuario usuario = usuarioService.autenticar(loginRequest.getDni(), loginRequest.getPassword());
        return toDto(usuario);
    }

    // Método helper
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
}