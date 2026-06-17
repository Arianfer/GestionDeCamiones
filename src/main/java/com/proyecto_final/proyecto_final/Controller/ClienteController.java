package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.ClienteRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.ClienteResponseDTO;
import com.proyecto_final.proyecto_final.Model.Cliente;
import com.proyecto_final.proyecto_final.Service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public List<ClienteResponseDTO> listar() {
        return clienteService.listarClientes();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Long id) {
        return clienteService.mapearToDTO(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ClienteResponseDTO guardar(@Valid @RequestBody ClienteRequestDTO clienteRequest) {
        return clienteService.crearCliente(clienteRequest);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable Long id,@Valid @RequestBody ClienteRequestDTO datosNuevos) {

        return clienteService.actualizarCliente(id, datosNuevos);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }


}