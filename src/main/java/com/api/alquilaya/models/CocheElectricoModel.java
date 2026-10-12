package com.api.alquilaya.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@DiscriminatorValue("COCHE_ELECTRICO")
public class CocheElectricoModel extends CocheModel implements Electrico {

    @NotNull
    @Min(0)
    @Max(100)
    @Column(name = "nivel_bateria")
    private Integer nivelBateria;

    @NotNull
    @Positive
    @Column(name = "autonomia_maxima_km")
    private Integer autonomiaMaximaKm;

    @Override
    public BigDecimal calcularPrecioAlquiler(int dias) {
        return super.calcularPrecioAlquiler(dias).multiply(BigDecimal.valueOf(0.85));
    }

    @Override
    public void cargarBateria() {
        this.nivelBateria = 100;
    }

    public Integer getNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(Integer nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    public Integer getAutonomiaMaximaKm() {
        return autonomiaMaximaKm;
    }

    public void setAutonomiaMaximaKm(Integer autonomiaMaximaKm) {
        this.autonomiaMaximaKm = autonomiaMaximaKm;
    }

}
