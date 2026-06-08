package com.proyecto_final.proyecto_final.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.proyecto_final.proyecto_final.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
    private Rol rol;
    private boolean activo;
    private String token;
}
