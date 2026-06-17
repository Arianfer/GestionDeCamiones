package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.DTO.Request.ClienteRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.ClienteResponseDTO;
import com.proyecto_final.proyecto_final.Model.Cliente;
import com.proyecto_final.proyecto_final.Repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;


    public ClienteResponseDTO crearCliente(ClienteRequestDTO clienteDTO) {

        Cliente cliente = mapearToEntidad(clienteDTO);

        //Validaciones
        if (clienteRepository.existsByCuit(cliente.getCuit())) {
            throw new RuntimeException("Ya existe un cliente registrado con el CUIT: " + cliente.getCuit());
        }
        if (clienteRepository.existsByRazonSocial(cliente.getRazonSocial())) {
            throw new RuntimeException("Ya existe un cliente registrado con la Razón Social: " + cliente.getRazonSocial());
        }

        return mapearToDTO(clienteRepository.save(cliente));
    }

    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapearToDTO)
                .toList();
    }

    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con el ID: " + id));
    }

    public Cliente buscarPorCuit(String cuit) {
        return clienteRepository.findByCuit(cuit)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún cliente con el CUIT: " + cuit));
    }

    public ClienteResponseDTO actualizarCliente(Long id, ClienteRequestDTO datosNuevos) {
        Cliente clienteExistente = buscarPorId(id);

        // Ojo: Si van a editar el CUIT o Razón Social, habría que validar que no choquen
        // con otros, pero si solo editan datos menores o asumimos que el CUIT no cambia:
        clienteExistente.setRazonSocial(datosNuevos.getRazonSocial());
        clienteExistente.setCuit(datosNuevos.getCuit());

        return mapearToDTO(clienteRepository.save(clienteExistente));
    }

    public void eliminarCliente(Long id) {
        buscarPorId(id);
        clienteRepository.deleteById(id);
    }

    // MAPEOS
    public Cliente mapearToEntidad(ClienteRequestDTO dto) {
        return Cliente.builder()
                .cuit(dto.getCuit())
                .razonSocial(dto.getRazonSocial())
                .build();
    }
    public ClienteResponseDTO mapearToDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .cuit(cliente.getCuit())
                .razonSocial(cliente.getRazonSocial())
                .build();
    }
}