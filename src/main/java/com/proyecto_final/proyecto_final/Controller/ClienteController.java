package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Response.ClienteResponseDTO;
import com.proyecto_final.proyecto_final.Model.Cliente;
import com.proyecto_final.proyecto_final.Service.ClienteService;
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
        return clienteService.listarClientes().stream()
                .map(this::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Long id) {
        return toDto(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ClienteResponseDTO guardar(@RequestBody Cliente cliente) {
        return toDto(clienteService.crearCliente(cliente));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
        return toDto(clienteService.actualizarCliente(id, cliente));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }


    private ClienteResponseDTO toDto(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .cuit(cliente.getCuit())
                .razonSocial(cliente.getRazonSocial())
                .build();
    }
}