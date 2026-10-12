package com.api.alquilaya.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "usuarios")
public class UsuariosModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column (nullable = false, unique = true, length = 100)
    private String email;

    @Column (nullable = false, length = 255)
    private String password;

    @Column (nullable = false, length = 50)
    private String rol;

    @Column (nullable = false)
    private boolean activo;

    public UsuariosModel() {
    }

    public UsuariosModel(Long idUsuario, String email, String password, String rol, boolean activo) {
        this.idUsuario = idUsuario;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.activo = activo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    
    
}
