package com.proyecto_final.proyecto_final.Enums;

public enum TipoCamion {
    RECOLECTOR(11400.0, 0.55),       // Iveco Tector 170E28
    BARRIOS_PRIVADOS(10300.0, 0.45), // Iveco Tector Attack 150E21
    DAILY(4400.0, 0.18); ///Daily es el que reparte tachos o retira tachos (Iveco Daily 70C17)

    // SCRUM-151: capacidad (kg) y consumo de diésel (L/km) por defecto según el tipo.
    // Se usan cuando al crear un camión no se mandan esos valores.
    // Capacidad: carga útil del chasis según la ficha técnica de Iveco (incluye la caja).
    // Consumo: estimación a partir de estudios de recolección urbana; CIAGESER no registra
    // el consumo real. Se puede ajustar por camión al editarlo.
    private final Double capacidadPorDefecto;
    private final Double consumoPorDefecto;

    TipoCamion(Double capacidadPorDefecto, Double consumoPorDefecto) {
        this.capacidadPorDefecto = capacidadPorDefecto;
        this.consumoPorDefecto = consumoPorDefecto;
    }

    public Double getCapacidadPorDefecto() {
        return capacidadPorDefecto;
    }

    public Double getConsumoPorDefecto() {
        return consumoPorDefecto;
    }
}