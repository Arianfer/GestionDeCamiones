package com.proyecto_final.proyecto_final.DTO.Request;

import com.proyecto_final.proyecto_final.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequestDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String password;
    private Rol rol;
}