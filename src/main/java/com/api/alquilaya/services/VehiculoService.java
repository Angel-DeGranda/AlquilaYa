package com.api.alquilaya.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.alquilaya.models.CocheElectricoModel;
import com.api.alquilaya.models.FurgonetaModel;
import com.api.alquilaya.models.MotoModel;
import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;

import jakarta.transaction.Transactional;

@Service
public class VehiculoService {

    private final IVehiculoRepository vehiculoRepository;
    private final IOficinaAlquilerRepository oficinaRepository;

    public VehiculoService(IVehiculoRepository vehiculoRepository,
                           IOficinaAlquilerRepository oficinaRepository) {
        this.vehiculoRepository = vehiculoRepository;
        this.oficinaRepository = oficinaRepository;
    }

    public List<VehiculoModel> listarTodos() {
        return vehiculoRepository.findAll();
    }

    public List<VehiculoModel> listarDisponibles() {
        return vehiculoRepository.findByEstatus(VehiculoModel.VehiculoStatus.Disponible);
    }

    public VehiculoModel buscarPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vehículo no encontrado con id " + id));
    }

    private OficinaAlquilerModel obtenerOficina(VehiculoModel vehiculo) {
        if (vehiculo.getOficina().getIdOficina() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debes indicar la oficina (idOficina)");
        return oficinaRepository.findById(vehiculo.getOficina().getIdOficina())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Oficina no encontrada"));
    }

    public VehiculoModel crear(VehiculoModel vehiculo) {
        if (vehiculoRepository.existsByMatricula(vehiculo.getMatricula()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un vehículo con esa matrícula");

        vehiculo.setOficina(obtenerOficina(vehiculo));
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public VehiculoModel actualizar(Long id, VehiculoModel datos) {
        VehiculoModel vehiculo = buscarPorId(id);

        if (vehiculo.getClass() != datos.getClass())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede cambiar el tipo de un vehículo");
        if (!vehiculo.getMatricula().equals(datos.getMatricula())
                && vehiculoRepository.existsByMatricula(datos.getMatricula()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un vehículo con esa matrícula");

        vehiculo.setOficina(obtenerOficina(datos));
        vehiculo.setMarca(datos.getMarca());
        vehiculo.setModelo(datos.getModelo());
        vehiculo.setAnio(datos.getAnio());
        vehiculo.setMatricula(datos.getMatricula());
        vehiculo.setTarifaDiaria(datos.getTarifaDiaria());

        // atributos propios de cada subtipo
        if (vehiculo instanceof MotoModel m && datos instanceof MotoModel d) {
            m.setCilindrada(d.getCilindrada());
        } else if (vehiculo instanceof FurgonetaModel f && datos instanceof FurgonetaModel d) {
            f.setCapacidadCargaKg(d.getCapacidadCargaKg());
        } else if (vehiculo instanceof CocheElectricoModel c && datos instanceof CocheElectricoModel d) {
            c.setNivelBateria(d.getNivelBateria());
            c.setAutonomiaMaximaKm(d.getAutonomiaMaximaKm());
        }

        return vehiculoRepository.save(vehiculo);
    }

    public void eliminar(Long id) {
        if (!vehiculoRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado con id " + id);
        vehiculoRepository.deleteById(id);
    }
}
