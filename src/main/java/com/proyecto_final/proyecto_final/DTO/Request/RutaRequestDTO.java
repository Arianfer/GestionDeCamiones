package com.proyecto_final.proyecto_final.DTO.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RutaRequestDTO {
    private String nombre;
    private String descripcion;
}
