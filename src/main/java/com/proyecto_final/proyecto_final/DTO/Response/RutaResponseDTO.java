package com.proyecto_final.proyecto_final.DTO.Response;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutaResponseDTO {
    private Integer id;
    private String nombre;
    private String descripcion;
    private List<ServicioResponseDTO> servicios;
}
