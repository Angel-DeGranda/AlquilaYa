package com.api.alquilaya.services;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.alquilaya.models.UsuariosModel;
import com.api.alquilaya.repositories.IUsuariosRepository;

@Service 
public class UsuariosService {
    
    private final IUsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuariosService(IUsuariosRepository usuariosRepository, PasswordEncoder passwordEncoder) {
        this.usuariosRepository = usuariosRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuariosModel crearUsuario(String email, String password, String rol){
        if (usuariosRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        UsuariosModel usuario = new UsuariosModel();
        usuario.setEmail(email);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(rol);
        usuario.setActivo(true);

        return usuariosRepository.save(usuario);
    }

    public Optional<UsuariosModel> obtenerUsuarioPorEmail(String email){
        return usuariosRepository.findByEmail(email);
    }

}
