package com.proyecto_final.proyecto_final.Model;

import com.proyecto_final.proyecto_final.Enums.EstadoTarea;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
@Data
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    @Enumerated(EnumType.STRING)
    private EstadoTarea estado; // PENDIENTE, COMPLETADA, CANCELADA, INCOVENIENTE

    // La tarea sabe a qué camión y ruta pertenece directamente
    @ManyToOne
    @JoinColumn(name = "id_camion")
    private Camion camion;

    @ManyToOne
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

    // Si querés saber cuándo se hizo
    private LocalDateTime fechaEjecucion;
}