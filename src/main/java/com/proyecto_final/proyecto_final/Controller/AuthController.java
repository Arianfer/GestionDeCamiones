package com.proyecto_final.proyecto_final.Controller;
import com.proyecto_final.proyecto_final.DTO.UsuarioDTO;
import com.proyecto_final.proyecto_final.DTO.UsuarioLoginDTO;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public UsuarioDTO login(@RequestBody UsuarioLoginDTO loginRequest) {
        Usuario usuario = usuarioService.autenticar(loginRequest.getEmail(),loginRequest.getPassword());
        return toDto(usuario);
    }

    @PostMapping("/register")
    public UsuarioDTO register(@RequestBody Usuario usuario) {
        usuario.setActivo(true);
        return toDto(usuarioService.crearUsuario(usuario));
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
