package com.api.alquilaya.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CocheModel.class, name = "COCHE"),
    @JsonSubTypes.Type(value = MotoModel.class, name = "MOTO"),
    @JsonSubTypes.Type(value = FurgonetaModel.class, name = "FURGONETA"),
    @JsonSubTypes.Type(value = CocheElectricoModel.class, name = "COCHE_ELECTRICO")
})

@Entity
@Table(name = "vehiculo")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_vehiculo")
public abstract class VehiculoModel implements Alquilable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_oficina", nullable = false)
    private OficinaAlquilerModel oficina;

    @NotBlank
    @Size(max = 100)
    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @NotBlank
    @Size(max = 200)
    @Column(name = "modelo", nullable = false, length = 200)
    private String modelo;

    @NotNull
    @Column(name = "anio", nullable = false)
    private Year anio;

    @NotBlank
    @Size(max = 10)
    @Column(name = "matricula", nullable = false, unique = true, length = 10)
    private String matricula;

    @NotNull
    @Positive
    @Column(name = "tarifa_diaria", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarifaDiaria;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false, length = 20)
    private VehiculoStatus estatus = VehiculoStatus.Disponible;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;   // se llena sola con el @PrePersist

    public VehiculoModel() {
    }

    public VehiculoModel(Year anio, LocalDate fechaRegistro, Long idVehiculo, String marca, String matricula, String modelo, OficinaAlquilerModel oficina, BigDecimal tarifaDiaria) {
        this.anio = anio;
        this.fechaRegistro = fechaRegistro;
        this.idVehiculo = idVehiculo;
        this.marca = marca;
        this.matricula = matricula;
        this.modelo = modelo;
        this.oficina = oficina;
        this.tarifaDiaria = tarifaDiaria;
    }

    @PrePersist
    protected void alCrear() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDate.now();
        }
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public OficinaAlquilerModel getOficina() {
        return oficina;
    }

    public void setOficina(OficinaAlquilerModel oficina) {
        this.oficina = oficina;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Year getAnio() {
        return anio;
    }

    public void setAnio(Year anio) {
        this.anio = anio;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public BigDecimal getTarifaDiaria() {
        return tarifaDiaria;
    }

    public void setTarifaDiaria(BigDecimal tarifaDiaria) {
        this.tarifaDiaria = tarifaDiaria;
    }

    public VehiculoStatus getEstatus() {
        return estatus;
    }

    public void setEstatus(VehiculoStatus estatus) {
        this.estatus = estatus;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public enum VehiculoStatus {
        Disponible, Alquilado, Mantenimiento
    }

    public abstract BigDecimal calcularPrecioAlquiler(int dias);

    @Override
    public boolean estaDisponible() {
        return this.estatus == VehiculoStatus.Disponible;
    }

    @Override
    public void alquilar() {
        this.estatus = VehiculoStatus.Alquilado;
    }

    @Override
    public void devolver() {
        this.estatus = VehiculoStatus.Disponible;
    }

}
