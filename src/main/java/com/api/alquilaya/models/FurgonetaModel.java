package com.api.alquilaya.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@DiscriminatorValue("FURGONETA")
public class FurgonetaModel extends VehiculoModel {

    @NotNull
    @Positive
    @Column(name = "capacidad_carga_kg")
    private Integer capacidadCargaKg;

    @Override
    public BigDecimal calcularPrecioAlquiler(int dias) {
        BigDecimal total = getTarifaDiaria().multiply(BigDecimal.valueOf(dias));
        if (capacidadCargaKg != null && capacidadCargaKg > 1000) {
            total = total.add(BigDecimal.valueOf(15).multiply(BigDecimal.valueOf(dias)));
        }
        return total;
    }

    public Integer getCapacidadCargaKg() {
        return capacidadCargaKg;
    }

    public void setCapacidadCargaKg(Integer capacidadCargaKg) {
        this.capacidadCargaKg = capacidadCargaKg;
    }
}
