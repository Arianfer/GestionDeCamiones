package com.proyecto_final.proyecto_final.Controller;

import com.proyecto_final.proyecto_final.DTO.Request.RutaRequestDTO;
import com.proyecto_final.proyecto_final.DTO.Response.RutaResponseDTO;
import com.proyecto_final.proyecto_final.DTO.Response.ServicioResponseDTO;
import com.proyecto_final.proyecto_final.Model.Ruta;
import com.proyecto_final.proyecto_final.Model.Usuario;
import com.proyecto_final.proyecto_final.Service.RutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rutas")
@RequiredArgsConstructor
public class RutaController {

    private final RutaService rutaService;

    @GetMapping
    public List<RutaResponseDTO> listarRutas(){
        return rutaService.listarRutas().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public RutaResponseDTO crearRuta(@Valid @RequestBody RutaRequestDTO rutaDTO){
        // Transformamos el JSON de entrada en un objeto de Java
        Ruta rutaCruda = mapearEntidad(rutaDTO);
        // Lo mandamos al Service (que ahora le pone la fecha si falta y busca al chofer)
        Ruta rutaGuardada = rutaService.crearRuta(rutaCruda);
        // Lo devolvemos limpio al Frontend
        return convertirADto(rutaGuardada);
    }

    @GetMapping("/{id}")
    public RutaResponseDTO obtenerPorId(@PathVariable Long id){
        return convertirADto(rutaService.buscarPorId(id));
    }

    @GetMapping("/nombre/{nombre}")
    public RutaResponseDTO obtenerPorNombre(@PathVariable String nombre){
        return convertirADto(rutaService.buscarPorNombre(nombre));
    }

    @PutMapping("/{id}")
    public RutaResponseDTO actualizarRuta(@PathVariable Long id, @Valid @RequestBody RutaRequestDTO rutaDTO){
        Ruta rutaCruda = mapearEntidad(rutaDTO);
        Ruta rutaActualizada = rutaService.actualizarRuta(id, rutaCruda);
        return convertirADto(rutaActualizada);
    }

    @DeleteMapping("/{id}")
    public void eliminarRuta(@PathVariable Long id){
        rutaService.eliminarRuta(id);
    }

    // =======================================================
    // MÉTODOS HELPER (Para traducir entre Entidad y DTO)
    // =======================================================

    private Ruta mapearEntidad(RutaRequestDTO dto) {
        Ruta ruta = new Ruta();
        ruta.setNombre(dto.getNombre());
        ruta.setDescripcion(dto.getDescripcion());
        ruta.setFecha(dto.getFecha());

        // Creamos un Usuario "cascarón" solo con el ID.
        // El RutaService se encarga de ir a buscar el resto de los datos a la BD.
        if (dto.getIdChofer() != null) {
            Usuario chofer = new Usuario();
            chofer.setId(dto.getIdChofer());
            ruta.setChofer(chofer);
        }

        return ruta;
    }

    private RutaResponseDTO convertirADto(Ruta ruta) {
        // 1. Mapeamos la lista de Servicios a DTOs (si es que la ruta tiene servicios asignados)
        List<ServicioResponseDTO> listaServicios = null;

        if (ruta.getServicios() != null && !ruta.getServicios().isEmpty()) {
            listaServicios = ruta.getServicios().stream()
                    .map(servicio -> ServicioResponseDTO.builder()
                            .id(servicio.getId())
                            .nombre(servicio.getNombre())
                            .prioridad(servicio.getPrioridad())
                            .direccion(servicio.getDireccion())
                            .tipoResiduo(servicio.getTipoResiduo())
                            .frecuencia(servicio.getFrecuencia())
                            .latitud(servicio.getLatitud())
                            .longitud(servicio.getLongitud())
                            .orden(servicio.getOrden())
                            // Evitamos errores de NullPointer si un servicio no tiene ruta o cliente explícito cargado en memoria
                            .idRuta(ruta.getIdRuta())
                            .idCliente(servicio.getCliente() != null ? servicio.getCliente().getId() : null)
                            .build())
                    .collect(Collectors.toList());
        }

        // 2. Armamos el DTO principal
        return RutaResponseDTO.builder()
                .idRuta(ruta.getIdRuta())
                .nombre(ruta.getNombre())
                .descripcion(ruta.getDescripcion())
                .fecha(ruta.getFecha())
                .idChofer(ruta.getChofer() != null ? ruta.getChofer().getId() : null)
                .nombreChofer(ruta.getChofer() != null ? ruta.getChofer().getNombre() + " " + ruta.getChofer().getApellido() : null)
                .servicios(listaServicios) // <-- ¡Acá inyectamos la lista ya procesada!
                .build();
    }
}