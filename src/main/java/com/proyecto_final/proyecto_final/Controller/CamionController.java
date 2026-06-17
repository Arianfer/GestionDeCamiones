package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.CamionRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.CamionResponseDTO;
import com.proyecto_final.proyecto_final.Model.Camion;
import com.proyecto_final.proyecto_final.Service.CamionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/camiones")
public class CamionController {

    @Autowired
    private CamionService camionService;

    @GetMapping
    public List<CamionResponseDTO> listar() {
        return camionService.listarCamiones();
    }

    @PostMapping
    public CamionResponseDTO guardar(@Valid @RequestBody CamionRequestDTO camiondto) {
        return camionService.crearCamion(camiondto);
    }

    @GetMapping("/{id}")
    public Camion obtenerPorId(@PathVariable Long id) {
        return camionService.buscarPorId(id);
    }

    @GetMapping("/{id}/costo-combustible")
    public Double calcularCostoCombustible(@PathVariable Long id, @RequestParam Double km) {
        return camionService.calcularCostoViaje(id, km);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        camionService.eliminarCamion(id);
    }
}