package com.api.alquilaya.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;

import jakarta.transaction.Transactional;

@Service
public class OficinaAlquilerService {

    private final IOficinaAlquilerRepository oficinaRepository;

    public OficinaAlquilerService(IOficinaAlquilerRepository oficinaRepository) {
        this.oficinaRepository = oficinaRepository;
    }

    public List<OficinaAlquilerModel> listarTodas() {
        return oficinaRepository.findAll();
    }

    public OficinaAlquilerModel buscarPorId(Long id) {
        return oficinaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Oficina no encontrada con id " + id));
    }

    public OficinaAlquilerModel guardar(OficinaAlquilerModel oficina) {
        return oficinaRepository.save(oficina);
    }

    @Transactional
    public OficinaAlquilerModel actualizar(Long id, OficinaAlquilerModel datos) {
        OficinaAlquilerModel oficina = buscarPorId(id);
        oficina.setNombreOficina(datos.getNombreOficina());
        oficina.setDireccion(datos.getDireccion());
        oficina.setCiudad(datos.getCiudad());
        oficina.setRegion(datos.getRegion());
        return oficinaRepository.save(oficina);
    }

    public void eliminar(Long id) {
        if (!oficinaRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Oficina no encontrada con id " + id);
        oficinaRepository.deleteById(id);
    }
}
