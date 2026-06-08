package com.proyecto_final.proyecto_final.Controller;
import com.proyecto_final.proyecto_final.Model.Ruta;
import com.proyecto_final.proyecto_final.Service.RutaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor

public class RutaController {

    @Autowired
    private RutaService rutaService;

    @GetMapping
    public List<Ruta> listarRutas(){
        return rutaService.listarRutas();
    }

    @PostMapping
    public Ruta crearRuta(@RequestBody Ruta ruta){
        return rutaService.crearRuta(ruta);
    }

    @GetMapping("/{id}")
    public Ruta obtenerPorId(@PathVariable Long id){
        return rutaService.buscarPorId(id);
    }

    @GetMapping("/nombre/{nombre}")
    public Ruta obtenerPorNombre(@PathVariable String nombre){
        return rutaService.buscarPorNombre(nombre);
    }

    @PutMapping("/{id}")
    public Ruta actualizarRuta(@PathVariable Long id, @RequestBody Ruta ruta){
        return rutaService.actualizarRuta(id, ruta);
    }

    @DeleteMapping("/{id}")
    public void eliminarRuta(@PathVariable Long id){
        rutaService.eliminarRuta(id);
    }



}
