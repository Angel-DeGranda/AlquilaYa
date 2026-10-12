package com.api.alquilaya.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "alquiler")
public class AlquilerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Long idAlquiler;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private VehiculoModel vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClienteModel cliente;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicioAlquiler;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFinAlquiler;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false, length = 20)
    private AlquilerStatus estatus = AlquilerStatus.Pendiente;

    @Column(name = "precio_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioTotal;

    public AlquilerModel() {
    }

    public AlquilerModel(ClienteModel cliente, LocalDate fechaFinAlquiler, LocalDate fechaInicioAlquiler, Long idAlquiler, BigDecimal precioTotal, VehiculoModel vehiculo) {
        this.cliente = cliente;
        this.fechaFinAlquiler = fechaFinAlquiler;
        this.fechaInicioAlquiler = fechaInicioAlquiler;
        this.idAlquiler = idAlquiler;
        this.precioTotal = precioTotal;
        this.vehiculo = vehiculo;
    }

    public Long getIdAlquiler() {
        return idAlquiler;
    }

    public void setIdAlquiler(Long idAlquiler) {
        this.idAlquiler = idAlquiler;
    }

    public VehiculoModel getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(VehiculoModel vehiculo) {
        this.vehiculo = vehiculo;
    }

    public ClienteModel getCliente() {
        return cliente;
    }

    public void setCliente(ClienteModel cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaInicioAlquiler() {
        return fechaInicioAlquiler;
    }

    public void setFechaInicioAlquiler(LocalDate fechaInicioAlquiler) {
        this.fechaInicioAlquiler = fechaInicioAlquiler;
    }

    public LocalDate getFechaFinAlquiler() {
        return fechaFinAlquiler;
    }

    public void setFechaFinAlquiler(LocalDate fechaFinAlquiler) {
        this.fechaFinAlquiler = fechaFinAlquiler;
    }

    public AlquilerStatus getEstatus() {
        return estatus;
    }

    public void setEstatus(AlquilerStatus estatus) {
        this.estatus = estatus;
    }

    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }

    public enum AlquilerStatus {
        Pendiente, Activo, Completado
    }

}
