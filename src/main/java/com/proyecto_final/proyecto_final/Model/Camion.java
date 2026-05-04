package com.proyecto_final.proyecto_final.Model;

import com.proyecto_final.proyecto_final.Enums.EstadoCamion;
import com.proyecto_final.proyecto_final.Enums.TipoCamion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "camion")
@Data // Genera Getters, Setters, toString, equals y hashCode automáticamente
@NoArgsConstructor // Genera el constructor vacío (obligatorio para JPA)
@AllArgsConstructor // Genera un constructor con todos los atributos

public class Camion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true,length = 10)
    private String patente;

    @Enumerated(EnumType.STRING)
    private TipoCamion tipo;

    @Enumerated(EnumType.STRING)
    private EstadoCamion estadoCamion;

    private Double capacidadCarga;
    private Double consumoDieselPorKm;


}
