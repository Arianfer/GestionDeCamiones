package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.ClienteRequestDTO;
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
                .map(this::toResponseDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO obtenerPorId(@PathVariable Long id) {
        return toResponseDto(clienteService.buscarPorId(id));
    }

    @PostMapping
    public ClienteResponseDTO guardar(@RequestBody ClienteRequestDTO clienteRequest) {
        // Convertimos el DTO de entrada en Entidad para el Service
        Cliente cliente = toEntity(clienteRequest);
        return toResponseDto(clienteService.crearCliente(cliente));
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO actualizar(@PathVariable Long id, @RequestBody ClienteRequestDTO clienteRequest) {
        // Convertimos el DTO de entrada en Entidad para el Service
        Cliente datosNuevos = toEntity(clienteRequest);
        return toResponseDto(clienteService.actualizarCliente(id, datosNuevos));
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }

    // Mapeo: Request DTO -> Entidad (Para recibir datos)
    private Cliente toEntity(ClienteRequestDTO dto) {
        return Cliente.builder()
                .cuit(dto.getCuit())
                .razonSocial(dto.getRazonSocial())
                .build();
    }

    // Mapeo: Entidad -> Response DTO (Para enviar datos)
    private ClienteResponseDTO toResponseDto(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .cuit(cliente.getCuit())
                .razonSocial(cliente.getRazonSocial())
                .build();
    }
}