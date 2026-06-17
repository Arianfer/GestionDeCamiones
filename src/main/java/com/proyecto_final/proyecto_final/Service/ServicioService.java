package com.proyecto_final.proyecto_final.Service;

import com.proyecto_final.proyecto_final.DTO.Response.ServicioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Servicio;
import com.proyecto_final.proyecto_final.Repository.ServicioRepository;
import com.proyecto_final.proyecto_final.Repository.RutaRepository;
import com.proyecto_final.proyecto_final.Repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final RutaRepository rutaRepository;
    private final ClienteRepository clienteRepository;

    public Servicio crearServicio(Servicio servicio) {
        // Validaciones ruta
        if (servicio.getRuta() != null) {
            Long rutaId = servicio.getRuta().getIdRuta();
            rutaRepository.findById(rutaId)
                    .orElseThrow(() -> new RuntimeException("La ruta especificada no existe"));
        } else {
            throw new RuntimeException("El servicio debe estar asignado a una ruta");
        }
        // Validacion cliente
        if (servicio.getCliente() != null && servicio.getCliente().getId() != null) {
            clienteRepository.findById(servicio.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("El cliente asociado no existe"));
        } else {
            throw new RuntimeException("El servicio debe estar asociado a un cliente obligatorio");
        }

        return servicioRepository.save(servicio);
    }

    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    public Servicio buscarPorId(Long id) {
        return servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + id));
    }

    public Servicio actualizarServicio(Long id, Servicio datosNuevos) {
        Servicio servicioExistente = buscarPorId(id);

        servicioExistente.setNombre(datosNuevos.getNombre());
        servicioExistente.setPrioridad(datosNuevos.getPrioridad());
        servicioExistente.setDireccion(datosNuevos.getDireccion());
        servicioExistente.setTipoResiduo(datosNuevos.getTipoResiduo());
        servicioExistente.setFrecuencia(datosNuevos.getFrecuencia());
        servicioExistente.setLatitud(datosNuevos.getLatitud());
        servicioExistente.setLongitud(datosNuevos.getLongitud());
        servicioExistente.setOrden(datosNuevos.getOrden());

        if (datosNuevos.getRuta() != null) {
            Long nuevaRutaId = (long) datosNuevos.getRuta().getIdRuta();
            servicioExistente.setRuta(rutaRepository.findById(nuevaRutaId).orElseThrow());
        }

        return servicioRepository.save(servicioExistente);
    }

    public void eliminarServicio(Long id) {
        buscarPorId(id);
        servicioRepository.deleteById(id);
    }

    @Transactional
    public void actualizarOrdenServicios(List<Long> idsServiciosOrdenados) {
        for (int i = 0; i < idsServiciosOrdenados.size(); i++) {
            Long idServicio = idsServiciosOrdenados.get(i);
            Servicio servicio = buscarPorId(idServicio);
            servicio.setOrden(i + 1); // El orden arranca en 1, 2, 3...
            servicioRepository.save(servicio);
        }
    }

}
