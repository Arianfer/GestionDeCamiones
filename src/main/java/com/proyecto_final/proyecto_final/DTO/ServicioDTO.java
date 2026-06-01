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
    private int orden;
    private Long idRuta;
    private  Double latitudRuta;
    private Double longitudRuta;
}
