package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.UsuarioLoginRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.UsuarioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.JwtService; // <-- Importamos el servicio
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtService jwtService; // <-- Lo inyectamos acá

    @PostMapping("/login")
    public UsuarioResponseDTO login(@RequestBody UsuarioLoginRequestDTO loginRequest) {
        // 1. Verificamos credenciales
        Usuario usuario = usuarioService.autenticar(loginRequest.getDni(), loginRequest.getPassword());

        // 2. Fabricamos el Token JWT
        String tokenJwt = jwtService.generarToken(usuario);

        // 3. Devolvemos todo junto
        return convertirADto(usuario, tokenJwt);
    }

    private UsuarioResponseDTO convertirADto(Usuario usuario, String token) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .dni(usuario.getDni())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .token(token) // <-- Se lo metemos a la respuesta
                .build();
    }
}