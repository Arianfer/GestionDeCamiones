package com.proyecto_final.proyecto_final.DTO.Response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteResponseDTO {
    private Long id;
    private String cuit;
    private String razonSocial;
}