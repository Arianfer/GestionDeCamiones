package com.proyecto_final.proyecto_final.Model;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tarea")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private LocalDateTime fechaEjecucion;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado;

    // La tarea sabe a qué camión y ruta pertenece directamente
    @ManyToOne
    @JoinColumn(name = "id_camion")
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

    // La tarea tiene un Usuario(Chofer) asignado
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}