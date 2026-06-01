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
    private String nombre; // Ej: "Manolo Alem", "Manolo Santa Fe"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridad prioridad;

    @Column(nullable = false)
    private String direccion; // Ej: "Alem 3800"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoResiduo tipoResiduo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frecuencia frecuencia;

    private Double latitud;
    private Double longitud;
    private int orden;

    // Muchas sucursales pertenecen a una misma Ruta de recolección
    @ManyToOne
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;

    // Muchas sucursales pertenecen al mismo Cliente (Mismo CUIT)
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}