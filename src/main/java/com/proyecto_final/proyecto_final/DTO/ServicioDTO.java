package com.proyecto_final.proyecto_final.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioDTO {
    private Long id;
    private String nombre;
    private int prioridad;
    private String direccion;
    private int orden;
    private Double latitud;
    private Double longitud;
    private Long idRuta;
    private Long idCliente;
}