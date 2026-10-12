package com.api.alquilaya.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.api.alquilaya.exception.RecursoDuplicadoException;
import com.api.alquilaya.exception.RecursoNoEncontradoException;
import com.api.alquilaya.exception.ReglaNegocioException;
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

    public VehiculoService(IVehiculoRepository vehiculoRepository, IOficinaAlquilerRepository oficinaRepository) {
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
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con id " + id));
    }

    public VehiculoModel crear(VehiculoModel vehiculo) {
        if (vehiculoRepository.existsByMatricula(vehiculo.getMatricula())) {
            throw new RecursoDuplicadoException("Ya existe un vehículo con esa matrícula");
        }
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional
    public VehiculoModel actualizar(Long id, VehiculoModel datos) {
        VehiculoModel vehiculo = buscarPorId(id);

        if (vehiculo.getClass() != datos.getClass()) {
            throw new ReglaNegocioException("No se puede cambiar el tipo de un vehículo");
        }
        if (!vehiculo.getMatricula().equals(datos.getMatricula())
                && vehiculoRepository.existsByMatricula(datos.getMatricula())) {
            throw new RecursoDuplicadoException("Ya existe un vehículo con esa matrícula");
        }

        if (datos.getOficina().getIdOficina() == null) {
            throw new ReglaNegocioException("Debes indicar la oficina (idOficina)");
        }
        OficinaAlquilerModel oficina = oficinaRepository.findById(datos.getOficina().getIdOficina())
                .orElseThrow(() -> new RecursoNoEncontradoException("Oficina no encontrada"));

        vehiculo.setOficina(oficina);
        vehiculo.setMarca(datos.getMarca());
        vehiculo.setModelo(datos.getModelo());
        vehiculo.setAnio(datos.getAnio());
        vehiculo.setMatricula(datos.getMatricula());
        vehiculo.setTarifaDiaria(datos.getTarifaDiaria());

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
        if (!vehiculoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Vehículo no encontrado con id " + id);
        }
        vehiculoRepository.deleteById(id);
    }
}
