package com.api.alquilaya.models;

import java.math.BigDecimal;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("COCHE")
public class CocheModel extends VehiculoModel{
    @Override 
    public BigDecimal calcularPrecioAlquiler(int dias){
        BigDecimal total = getTarifaDiaria().multiply(BigDecimal.valueOf(dias));
        if (dias >= 7) total = total.multiply(BigDecimal.valueOf(0.9));
        return total;
    }
}
