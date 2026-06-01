package com.proyecto_final.proyecto_final.DTO.Request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRequestDTO {
    private String cuit;
    private String razonSocial;
}