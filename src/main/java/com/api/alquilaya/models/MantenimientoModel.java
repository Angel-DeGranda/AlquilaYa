package com.api.alquilaya.models;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mantenimiento")
public class MantenimientoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mantenimiento")
    private Long idMantenimiento;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private VehiculoModel vehiculo;

    @ManyToOne
    @JoinColumn(name = "id_trabajador_responsable", nullable = false)
    private TrabajadorModel trabajadorResponsable;

    @ManyToOne
    @JoinColumn(name = "id_oficina_responsable", nullable = false)
    private OficinaAlquilerModel oficinaResponsable;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicioMantenimiento;

    @Column(name = "fecha_fin")
    private LocalDate fechaFinMantenimiento;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "costo", nullable = false, precision = 10, scale = 2)
    private BigDecimal costo;

    @Column(name = "kilometraje", nullable = false)
    private Long kilometraje;

    public MantenimientoModel() {
    }

    public MantenimientoModel(BigDecimal costo, String descripcion, LocalDate fechaFinMantenimiento, LocalDate fechaInicioMantenimiento, Long idMantenimiento, Long kilometraje, OficinaAlquilerModel oficinaResponsable, TrabajadorModel trabajadorResponsable, VehiculoModel vehiculo) {
        this.costo = costo;
        this.descripcion = descripcion;
        this.fechaFinMantenimiento = fechaFinMantenimiento;
        this.fechaInicioMantenimiento = fechaInicioMantenimiento;
        this.idMantenimiento = idMantenimiento;
        this.kilometraje = kilometraje;
        this.oficinaResponsable = oficinaResponsable;
        this.trabajadorResponsable = trabajadorResponsable;
        this.vehiculo = vehiculo;
    }

    public Long getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Long idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public VehiculoModel getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(VehiculoModel vehiculo) {
        this.vehiculo = vehiculo;
    }

    public TrabajadorModel getTrabajadorResponsable() {
        return trabajadorResponsable;
    }

    public void setTrabajadorResponsable(TrabajadorModel trabajadorResponsable) {
        this.trabajadorResponsable = trabajadorResponsable;
    }

    public OficinaAlquilerModel getOficinaResponsable() {
        return oficinaResponsable;
    }

    public void setOficinaResponsable(OficinaAlquilerModel oficinaResponsable) {
        this.oficinaResponsable = oficinaResponsable;
    }

    public LocalDate getFechaInicioMantenimiento() {
        return fechaInicioMantenimiento;
    }

    public void setFechaInicioMantenimiento(LocalDate fechaInicioMantenimiento) {
        this.fechaInicioMantenimiento = fechaInicioMantenimiento;
    }

    public LocalDate getFechaFinMantenimiento() {
        return fechaFinMantenimiento;
    }

    public void setFechaFinMantenimiento(LocalDate fechaFinMantenimiento) {
        this.fechaFinMantenimiento = fechaFinMantenimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Long getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(Long kilometraje) {
        this.kilometraje = kilometraje;
    }

}
