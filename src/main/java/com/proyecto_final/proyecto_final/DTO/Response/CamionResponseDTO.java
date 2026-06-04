package com.proyecto_final.proyecto_final.DTO.Response;

import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamionResponseDTO {
    private Long id;
    private String patente;
    private String tipo;
    private EstadoCamion estado;
    private Double capacidadCarga;
    private Double consumoDieselPorKm;
}