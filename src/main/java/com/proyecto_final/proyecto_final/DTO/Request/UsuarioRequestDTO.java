package com.proyecto_final.proyecto_final.DTO.Request;

import com.proyecto_final.proyecto_final.Enums.Rol;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private Rol rol;
}