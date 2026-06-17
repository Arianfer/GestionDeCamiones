package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioLoginRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.AuthResponseDTO;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.JwtService;
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    // Login - devuelve token JWT junto con los datos básicos del usuario
    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody UsuarioLoginRequestDTO loginRequest) {
        // Verificamos credenciales
        Usuario usuario = usuarioService.autenticar(loginRequest.getDni(), loginRequest.getPassword());

        // Generamos el token JWT
        String tokenJwt = jwtService.generarToken(usuario);

        // Devolvemos datos del usuario + token
        return convertirADto(usuario, tokenJwt);
    }

    // Convierte el usuario autenticado a AuthResponseDTO incluyendo el token
    private AuthResponseDTO convertirADto(Usuario usuario, String token) {
        return AuthResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .rol(usuario.getRol().name())
                .token(token)
                .build();
    }
}