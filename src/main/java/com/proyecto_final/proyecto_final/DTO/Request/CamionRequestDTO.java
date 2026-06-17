package com.proyecto_final.proyecto_final.DTO.Request;


import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Enums.TipoCamion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CamionRequestDTO {

    @NotBlank(message = "La patente es obligatoria")
    @Pattern(
            regexp = "^[A-Z]{2}[0-9]{3}[A-Z]{2}$|^[A-Z]{3}[0-9]{3}$",
            message = "La patente debe tener un formato válido"
    )
    private String patente;

    @NotNull(message = "Debe especificar un tipo de camión")
    private TipoCamion tipo;

    @NotNull(message = "Debe especificar un estado")
    private EstadoCamion estadoCamion;

    @NotNull(message = "La capacidad de carga es obligatoria")
    @Positive(message = "La capacidad de carga debe ser mayor a 0")
    private Double capacidadCarga;

    @NotNull(message = "El consumo de diésel es obligatorio")
    @Positive(message = "El consumo de diésel debe ser mayor a 0")
    private Double consumoDieselPorKm;
}
