package com.api.alquilaya.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "clientes")
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;

    @NotBlank
    @Size(max = 35)
    @Column(name = "nombre", nullable = false, length = 35)
    private String nombre;

    @NotBlank
    @Size(max = 35)
    @Column(name = "apellidos", nullable = false, length = 35)
    private String apellidos;

    @NotBlank
    @Email
    @Size(max = 50)
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @NotBlank
    @Size(max = 15)
    @Column(name = "telefono", nullable = false, length = 15)
    private String telefono;

    @NotNull
    @Past
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotBlank
    @Size(max = 20)
    @Column(name = "licencia_conducir", nullable = false, unique = true, length = 20)
    private String licenciaConducir;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = false, length = 20)
    private ClienteStatus estatus = ClienteStatus.Activo;

    @Size(min = 8, max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    public ClienteModel() {
    }

    public ClienteModel(String apellidos, String email, LocalDate fechaNacimiento, Long idCliente, String licenciaConducir, String nombre, String password, String telefono) {
        this.apellidos = apellidos;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.idCliente = idCliente;
        this.licenciaConducir = licenciaConducir;
        this.nombre = nombre;
        this.password = password;
        this.telefono = telefono;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getLicenciaConducir() {
        return licenciaConducir;
    }

    public void setLicenciaConducir(String licenciaConducir) {
        this.licenciaConducir = licenciaConducir;
    }

    public ClienteStatus getEstatus() {
        return estatus;
    }

    public void setEstatus(ClienteStatus estatus) {
        this.estatus = estatus;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public enum ClienteStatus {
        Activo, Inactivo, Suspendido
    }

}
