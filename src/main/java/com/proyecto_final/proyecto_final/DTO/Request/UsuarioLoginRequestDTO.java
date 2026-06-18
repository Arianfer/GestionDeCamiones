package com.proyecto_final.proyecto_final.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioLoginRequestDTO {
    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(
            regexp = "\\d{7,8}",
            message = "El DNI debe contener 7 u 8 números"
    )
    private String dni;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}

