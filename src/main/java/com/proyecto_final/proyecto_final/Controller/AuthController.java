package com.proyecto_final.proyecto_final.Controller;
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
    public Usuario login(@RequestBody Usuario loginRequest) {
        return  usuarioService.buscarPorEmail(loginRequest.getEmail());
    }

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
        return usuarioService.buscarPorEmail(usuario.getEmail());
    }
}
