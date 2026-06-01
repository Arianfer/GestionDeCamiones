package com.proyecto_final.proyecto_final.Model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String cuit;

    @Column(nullable = false)
    private String razonSocial; // Ej: "Churros Manolo S.A."

    // Un cliente puede tener muchos puntos de servicio
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Servicio> servicios;
}