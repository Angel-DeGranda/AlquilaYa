package com.api.alquilaya.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "oficina_alquier")
public class OficinaAlquilerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oficina")
    private Long idOficina;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre_oficina", nullable = false, length = 100)
    private String nombreOficina;

    @NotBlank
    @Size(max = 200)
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @NotBlank
    @Size(max = 50)
    @Column(name = "ciudad", nullable = false, length = 50)
    private String ciudad;

    @Size(max = 50)
    @Column(name = "region", length = 50)
    private String region;

    public Long getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Long idOficina) {
        this.idOficina = idOficina;
    }

    public String getNombreOficina() {
        return nombreOficina;
    }

    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
