package com.api.alquilaya.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.models.TrabajadorModel;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;
import com.api.alquilaya.repositories.ITrabajadorRepository;

import jakarta.transaction.Transactional;

@Service
public class TrabajadorService {

    private final ITrabajadorRepository trabajadorRepository;
    private final IOficinaAlquilerRepository oficinaRepository;
    private final PasswordEncoder passwordEncoder;

    public TrabajadorService(ITrabajadorRepository trabajadorRepository,
                             IOficinaAlquilerRepository oficinaRepository,
                             PasswordEncoder passwordEncoder) {
        this.trabajadorRepository = trabajadorRepository;
        this.oficinaRepository = oficinaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<TrabajadorModel> listarTodos() {
        return trabajadorRepository.findAll();
    }

    public TrabajadorModel buscarPorId(Long id) {
        return trabajadorRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Trabajador no encontrado con id " + id));
    }

    private OficinaAlquilerModel obtenerOficina(TrabajadorModel trabajador) {
        if (trabajador.getOficina().getIdOficina() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar la oficina (idOficina)");
        return oficinaRepository.findById(trabajador.getOficina().getIdOficina())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Oficina no encontrada"));
    }

    public TrabajadorModel crear(TrabajadorModel trabajador) {
        if (trabajador.getPassword() == null || trabajador.getPassword().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        if (trabajadorRepository.existsByEmail(trabajador.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un trabajador con ese email");
        if (trabajadorRepository.existsByLicenciaConducir(trabajador.getLicenciaConducir()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un trabajador con esa licencia");

        trabajador.setOficina(obtenerOficina(trabajador));
        trabajador.setPassword(passwordEncoder.encode(trabajador.getPassword()));
        return trabajadorRepository.save(trabajador);
    }

    @Transactional
    public TrabajadorModel actualizar(Long id, TrabajadorModel datos) {
        TrabajadorModel trabajador = buscarPorId(id);

        if (!trabajador.getEmail().equals(datos.getEmail()) && trabajadorRepository.existsByEmail(datos.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un trabajador con ese email");
        if (!trabajador.getLicenciaConducir().equals(datos.getLicenciaConducir())
                && trabajadorRepository.existsByLicenciaConducir(datos.getLicenciaConducir()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un trabajador con esa licencia");

        trabajador.setOficina(obtenerOficina(datos));
        trabajador.setNombre(datos.getNombre());
        trabajador.setApellidos(datos.getApellidos());
        trabajador.setEmail(datos.getEmail());
        trabajador.setTelefono(datos.getTelefono());
        trabajador.setFechaNacimiento(datos.getFechaNacimiento());
        trabajador.setLicenciaConducir(datos.getLicenciaConducir());
        trabajador.setEstatus(datos.getEstatus());
        if (datos.getPassword() != null && !datos.getPassword().isBlank())
            trabajador.setPassword(passwordEncoder.encode(datos.getPassword()));

        return trabajadorRepository.save(trabajador);
    }

    public void eliminar(Long id) {
        if (!trabajadorRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trabajador no encontrado con id " + id);
        trabajadorRepository.deleteById(id);
    }
}
