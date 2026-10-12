package com.api.alquilaya.services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.api.alquilaya.exception.RecursoDuplicadoException;
import com.api.alquilaya.exception.RecursoNoEncontradoException;
import com.api.alquilaya.exception.ReglaNegocioException;
import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.models.TrabajadorModel;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;
import com.api.alquilaya.repositories.ITrabajadorRepository;

import jakarta.transaction.Transactional;

@Service
public class TrabajadorService {

    private final ITrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder;
    private final IOficinaAlquilerRepository oficinaRepository;

    public TrabajadorService(ITrabajadorRepository trabajadorRepository, PasswordEncoder passwordEncoder, IOficinaAlquilerRepository oficinaRepository) {
        this.trabajadorRepository = trabajadorRepository;
        this.passwordEncoder = passwordEncoder;
        this.oficinaRepository = oficinaRepository;
    }

    public List<TrabajadorModel> listarTodos() {
        return trabajadorRepository.findAll();
    }

    public TrabajadorModel buscarPorId(Long id) {
        return trabajadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Trabajador no encontrado con id " + id));
    }

    public TrabajadorModel crear(TrabajadorModel trabajador) {
        if (trabajador.getPassword() == null || trabajador.getPassword().isBlank()) {
            throw new ReglaNegocioException("La contraseña es obligatoria");
        }
        if (trabajadorRepository.existsByEmail(trabajador.getEmail())) {
            throw new RecursoDuplicadoException("Ya existe un trabajador con ese email");
        }
        if (trabajadorRepository.existsByLicenciaConducir(trabajador.getLicenciaConducir())) {
            throw new RecursoDuplicadoException("Ya existe un trabajador con esa licencia");
        }

        trabajador.setPassword(passwordEncoder.encode(trabajador.getPassword()));
        return trabajadorRepository.save(trabajador);
    }

    @Transactional
    public TrabajadorModel actualizar(Long id, TrabajadorModel datos) {
        TrabajadorModel trabajador = buscarPorId(id);

        if (!trabajador.getEmail().equals(datos.getEmail()) && trabajadorRepository.existsByEmail(datos.getEmail())) {
            throw new RecursoDuplicadoException("Ya existe un trabajador con ese email");
        }
        if (!trabajador.getLicenciaConducir().equals(datos.getLicenciaConducir())
                && trabajadorRepository.existsByLicenciaConducir(datos.getLicenciaConducir())) {
            throw new RecursoDuplicadoException("Ya existe un trabajador con esa licencia");
        }

        if (datos.getOficina().getIdOficina() == null) {
            throw new ReglaNegocioException("Debes indicar la oficina (idOficina)");
        }
        OficinaAlquilerModel oficina = oficinaRepository.findById(datos.getOficina().getIdOficina())
                .orElseThrow(() -> new RecursoNoEncontradoException("Oficina no encontrada"));

        trabajador.setNombre(datos.getNombre());
        trabajador.setApellidos(datos.getApellidos());
        trabajador.setEmail(datos.getEmail());
        trabajador.setTelefono(datos.getTelefono());
        trabajador.setFechaNacimiento(datos.getFechaNacimiento());
        trabajador.setLicenciaConducir(datos.getLicenciaConducir());
        trabajador.setEstatus(datos.getEstatus());
        trabajador.setOficina(oficina);
        if (datos.getPassword() != null && !datos.getPassword().isBlank()) {
            trabajador.setPassword(passwordEncoder.encode(datos.getPassword()));
        }

        return trabajadorRepository.save(trabajador);
    }

    public void eliminar(Long id) {
        if (!trabajadorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Trabajador no encontrado con id " + id);
        }
        trabajadorRepository.deleteById(id);
    }
}
