package com.proyecto_final.proyecto_final.DTO.Response;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaResponseDTO {
    private Long id;
    private String descripcion;
    private LocalDateTime fechaEjecucion;
    private EstadoTarea estado;
    private CamionResponseDTO camion;
    private RutaResponseDTO ruta;
    private UsuarioResponseDTO usuario;
}
