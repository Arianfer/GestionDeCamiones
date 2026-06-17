package com.proyecto_final.proyecto_final.DTO.Response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String rol;
    private String token;
}