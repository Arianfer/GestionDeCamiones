package com.proyecto_final.proyecto_final.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequestDTO {
    @NotBlank(message = "El CUIT es obligatorio")
    @Pattern(
            regexp = "^[0-9]{11}$",
            message = "El CUIT debe contener exactamente 11 números"
    )
    private String cuit;

    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;
}