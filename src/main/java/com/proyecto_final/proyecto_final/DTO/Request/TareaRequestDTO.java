package com.proyecto_final.proyecto_final.DTO.Request;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TareaRequestDTO {
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 100, message = "La descripción no puede superar los 100 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha de ejecución es obligatoria")
    private LocalDateTime fechaEjecucion;

    @NotNull(message = "Debe especificar un estado")
    private EstadoTarea estado;

    @NotNull(message = "Debe especificar un camión")
    @Positive(message = "El ID del camión debe ser válido")
    private Long idCamion;

    @NotNull(message = "Debe especificar una ruta")
    @Positive(message = "El ID de ruta debe ser válido")
    private Long idRuta;

    @NotNull(message = "Debe especificar un usuario")
    @Positive(message = "El ID del usuario debe ser válido")
    private Long idUsuario;
}
