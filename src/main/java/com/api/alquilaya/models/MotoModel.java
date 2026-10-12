package com.api.alquilaya.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@DiscriminatorValue("MOTO")
public class MotoModel extends VehiculoModel {

    @NotNull
    @Positive
    @Column(name = "cilindrada")
    private Integer cilindrada;

    @Override
    public BigDecimal calcularPrecioAlquiler(int dias) {
        BigDecimal base = getTarifaDiaria().add(BigDecimal.valueOf(3));
        BigDecimal total = base.multiply(BigDecimal.valueOf(dias));
        if (cilindrada != null && cilindrada > 500) {
            total = total.add(BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(dias)));
        }
        return total;
    }

    public Integer getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(Integer cilindrada) {
        this.cilindrada = cilindrada;
    }

}
