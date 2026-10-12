package com.api.alquilaya.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.api.alquilaya.dto.MantenimientoRequest;
import com.api.alquilaya.exception.RecursoNoEncontradoException;
import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.models.TrabajadorModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IMantenimientoRepository;
import com.api.alquilaya.repositories.IOficinaAlquilerRepository;
import com.api.alquilaya.repositories.ITrabajadorRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;

import jakarta.transaction.Transactional;

@Service
public class MantenimientoService {

    private final IMantenimientoRepository mantenimientoRepository;
    private final IVehiculoRepository vehiculoRepository;
    private final ITrabajadorRepository trabajadorRepository;
    private final IOficinaAlquilerRepository oficinaRepository;

    public MantenimientoService(IMantenimientoRepository mantenimientoRepository, IVehiculoRepository vehiculoRepository, ITrabajadorRepository trabajadorRepository, IOficinaAlquilerRepository oficinaRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.oficinaRepository = oficinaRepository;
    }

    public List<MantenimientoModel> listarPorVehiculo(Long idVehiculo) {
        return mantenimientoRepository.findByVehiculo_IdVehiculo(idVehiculo);
    }

    @Transactional
    public MantenimientoModel crear(MantenimientoRequest request) {
        VehiculoModel vehiculo = vehiculoRepository.findById(request.idVehiculo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con id " + request.idVehiculo()));

        TrabajadorModel trabajador = trabajadorRepository.findById(request.idTrabajadorResponsable())
                .orElseThrow(() -> new RecursoNoEncontradoException("Trabajador no encontrado con id " + request.idTrabajadorResponsable()));

        // Si no mandan oficina, se usa la del vehículo
        OficinaAlquilerModel oficina = (request.idOficinaResponsable() == null)
                ? vehiculo.getOficina()
                : oficinaRepository.findById(request.idOficinaResponsable())
                        .orElseThrow(() -> new RecursoNoEncontradoException("Oficina no encontrada con id " + request.idOficinaResponsable()));

        MantenimientoModel mantenimiento = new MantenimientoModel();
        mantenimiento.setVehiculo(vehiculo);
        mantenimiento.setTrabajadorResponsable(trabajador);
        mantenimiento.setOficinaResponsable(oficina);
        mantenimiento.setFechaInicioMantenimiento(request.fechaInicioMantenimiento());
        mantenimiento.setDescripcion(request.descripcion());
        mantenimiento.setCosto(request.costo());
        mantenimiento.setKilometraje(request.kilometraje());

        vehiculo.setEstatus(VehiculoModel.VehiculoStatus.Mantenimiento);
        vehiculoRepository.save(vehiculo);

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public MantenimientoModel cerrar(Long idMantenimiento, LocalDate fechaFin) {
        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(() -> new RecursoNoEncontradoException("Mantenimiento no encontrado con id " + idMantenimiento));

        mantenimiento.setFechaFinMantenimiento(fechaFin);
        mantenimientoRepository.save(mantenimiento);

        VehiculoModel vehiculo = mantenimiento.getVehiculo();
        vehiculo.setEstatus(VehiculoModel.VehiculoStatus.Disponible);
        vehiculoRepository.save(vehiculo);

        return mantenimiento;
    }
}
