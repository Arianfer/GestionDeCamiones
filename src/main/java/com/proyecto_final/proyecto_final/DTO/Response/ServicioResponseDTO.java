package com.proyecto_final.proyecto_final.DTO.Response;

import com.proyecto_final.proyecto_final.Enums.Frecuencia;
import com.proyecto_final.proyecto_final.Enums.Prioridad;
import com.proyecto_final.proyecto_final.Enums.TipoResiduo;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioResponseDTO {
    private Long id;
    private String nombre;
    private int prioridad;
    private int orden;
    private Long idRuta;
    private Double latitudRuta;
    private Double longitudRuta;
}
