package com.proyecto_final.proyecto_final.Controller;

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
    public List<Usuario> listar() {
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable int id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

    @PutMapping
    public Usuario actualizarUsuario(@RequestBody int id, Usuario usuario) {
        return usuarioService.actualizarUsuario(id, usuario);
    }

    @PatchMapping("/{id}/desactivar")
    public void desactivarUsuario(@PathVariable int id) {
        usuarioService.desactivarUsuario(id);
    }


}
