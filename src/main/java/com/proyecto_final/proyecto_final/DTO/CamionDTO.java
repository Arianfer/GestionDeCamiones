package com.proyecto_final.proyecto_final.DTO;

import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamionDTO {
    private int id;
    private String patente;
    private String tipo;
    private EstadoCamion estado;

}
