package com.proyecto_final.proyecto_final.DTO.Request;

import com.proyecto_final.proyecto_final.Enums.Frecuencia;
import com.proyecto_final.proyecto_final.Enums.Prioridad;
import com.proyecto_final.proyecto_final.Enums.TipoResiduo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRequestDTO {
    private String nombre;
    private Prioridad prioridad;
    private String direccion;
    private TipoResiduo tipoResiduo;
    private Frecuencia frecuencia;
    private Integer idRuta;
}
