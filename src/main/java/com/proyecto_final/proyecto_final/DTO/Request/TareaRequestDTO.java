package com.proyecto_final.proyecto_final.DTO.Request;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaRequestDTO {
    private String descripcion;
    private LocalDateTime fechaEjecucion;
    private EstadoTarea estado;
    private Long idCamion;
    private Long idRuta;
    private Long idUsuario;
}
