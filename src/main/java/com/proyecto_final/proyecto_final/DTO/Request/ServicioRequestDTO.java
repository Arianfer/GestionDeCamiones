package com.proyecto_final.proyecto_final.DTO.Request;

import com.proyecto_final.proyecto_final.Enums.Frecuencia;
import com.proyecto_final.proyecto_final.Enums.Prioridad;
import com.proyecto_final.proyecto_final.Enums.TipoResiduo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "Debe especificar una prioridad")
    private Prioridad prioridad;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "Debe especificar un tipo de residuo")
    private TipoResiduo tipoResiduo;

    @NotNull(message = "Debe especificar una frecuencia")
    private Frecuencia frecuencia;

    @NotNull(message = "La latitud es obligatoria")
    private Double latitud;

    @NotNull(message = "La longitud es obligatoria")
    private Double longitud;

    @PositiveOrZero(message = "El orden no puede ser negativo")
    private int orden;

    @NotNull(message = "Debe especificar una ruta")
    @Positive(message = "El ID de ruta debe ser válido")
    private Long idRuta;

    @NotNull(message = "Debe especificar un cliente")
    @Positive(message = "El ID de cliente debe ser válido")
    private Long idCliente;
}