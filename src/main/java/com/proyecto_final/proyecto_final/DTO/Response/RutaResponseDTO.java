package com.proyecto_final.proyecto_final.DTO.Response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutaResponseDTO {
    private Long idRuta;
    private String nombre;
    private String descripcion;
    private List<ServicioResponseDTO> servicios;
    private LocalDate fecha;
    private Long idChofer;
    private String nombreChofer;
}
