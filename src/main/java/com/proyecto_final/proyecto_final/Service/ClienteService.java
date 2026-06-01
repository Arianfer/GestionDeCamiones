package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.Model.Cliente;
import com.proyecto_final.proyecto_final.Repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Crear un cliente con validaciones de unicidad
    public Cliente crearCliente(Cliente cliente) {
        // 1. Validar CUIT único
        if (clienteRepository.existsByCuit(cliente.getCuit())) {
            throw new RuntimeException("Ya existe un cliente registrado con el CUIT: " + cliente.getCuit());
        }

        // 2. Validar Razón Social única
        if (clienteRepository.existsByRazonSocial(cliente.getRazonSocial())) {
            throw new RuntimeException("Ya existe un cliente registrado con la Razón Social: " + cliente.getRazonSocial());
        }

        return clienteRepository.save(cliente);
    }

    // Listar todos los clientes (Útil para combos desplegables en el frontend)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Buscar por ID
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con el ID: " + id));
    }

    // Buscar por CUIT
    public Cliente buscarPorCuit(String cuit) {
        return clienteRepository.findByCuit(cuit)
                .orElseThrow(() -> new RuntimeException("No se encontró ningún cliente con el CUIT: " + cuit));
    }

    // Actualizar Cliente
    public Cliente actualizarCliente(Long id, Cliente datosNuevos) {
        Cliente clienteExistente = buscarPorId(id);

        // Ojo: Si van a editar el CUIT o Razón Social, habría que validar que no choquen
        // con otros, pero si solo editan datos menores o asumimos que el CUIT no cambia:
        clienteExistente.setRazonSocial(datosNuevos.getRazonSocial());
        clienteExistente.setCuit(datosNuevos.getCuit());

        return clienteRepository.save(clienteExistente);
    }

    // Eliminar Cliente
    public void eliminarCliente(Long id) {
        buscarPorId(id); // Valida existencia
        clienteRepository.deleteById(id);
    }
}