package com.proyecto_final.proyecto_final.Model;

import com.proyecto_final.proyecto_final.Enums.Frecuencia;
import com.proyecto_final.proyecto_final.Enums.Prioridad;
import com.proyecto_final.proyecto_final.Enums.TipoResiduo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "servicio")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Column(nullable = false)
    private String direccion;

    private Double latitud;
    private Double longitud;
    private int orden;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoResiduo tipoResiduo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frecuencia frecuencia;

    // Muchas zonas pertenecen a una ruta
    @ManyToOne
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;
}
