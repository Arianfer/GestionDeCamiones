package com.proyecto_final.proyecto_final.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UsuarioLoginDTO {
    private String email;
    private String password;
}

