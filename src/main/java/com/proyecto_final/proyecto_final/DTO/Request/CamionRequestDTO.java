package com.proyecto_final.proyecto_final.DTO.Request;


import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Enums.TipoCamion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamionRequestDTO {
    private String patente;
    private TipoCamion tipo;
    private EstadoCamion estadoCamion;
    private Double capacidadCarga;
    private Double consumoDieselPorKm;
}
