package com.api.alquilaya.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public MantenimientoService(IMantenimientoRepository mantenimientoRepository,
            IVehiculoRepository vehiculoRepository,
            ITrabajadorRepository trabajadorRepository,
            IOficinaAlquilerRepository oficinaRepository) {
        this.mantenimientoRepository = mantenimientoRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.oficinaRepository = oficinaRepository;
    }

    public List<MantenimientoModel> listarPorVehiculo(Long idVehiculo) {
        return mantenimientoRepository.findByVehiculo_IdVehiculo(idVehiculo);
    }

    @Transactional
    public MantenimientoModel crear(Long idVehiculo, Long idTrabajadorResponsable, Long idOficinaResponsable,
            LocalDate fechaInicioMantenimiento, String descripcion,
            BigDecimal costo, Long kilometraje) {

        VehiculoModel vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vehículo no encontrado con id " + idVehiculo));

        if (vehiculo.getEstatus() != VehiculoModel.VehiculoStatus.Disponible)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Solo un vehículo disponible puede enviarse a mantenimiento");

        TrabajadorModel trabajador = trabajadorRepository.findById(idTrabajadorResponsable)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Trabajador no encontrado con id " + idTrabajadorResponsable));

        // Si no mandan oficina, se usa la del vehículo
        OficinaAlquilerModel oficina = (idOficinaResponsable == null)
                ? vehiculo.getOficina()
                : oficinaRepository.findById(idOficinaResponsable)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Oficina no encontrada con id " + idOficinaResponsable));

        MantenimientoModel mantenimiento = new MantenimientoModel();
        mantenimiento.setVehiculo(vehiculo);
        mantenimiento.setTrabajadorResponsable(trabajador);
        mantenimiento.setOficinaResponsable(oficina);
        mantenimiento.setFechaInicioMantenimiento(fechaInicioMantenimiento);
        mantenimiento.setDescripcion(descripcion);
        mantenimiento.setCosto(costo);
        mantenimiento.setKilometraje(kilometraje);

        vehiculo.setEstatus(VehiculoModel.VehiculoStatus.Mantenimiento);
        vehiculoRepository.save(vehiculo);

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public MantenimientoModel cerrar(Long idMantenimiento, LocalDate fechaFin) {
        MantenimientoModel mantenimiento = mantenimientoRepository.findById(idMantenimiento)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Mantenimiento no encontrado con id " + idMantenimiento));

        if (mantenimiento.getFechaFinMantenimiento() != null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El mantenimiento ya fue cerrado");
        if (fechaFin.isBefore(mantenimiento.getFechaInicioMantenimiento()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de fin no puede ser anterior a la de inicio");

        mantenimiento.setFechaFinMantenimiento(fechaFin);
        mantenimientoRepository.save(mantenimiento);

        VehiculoModel vehiculo = mantenimiento.getVehiculo();
        vehiculo.setEstatus(VehiculoModel.VehiculoStatus.Disponible);
        vehiculoRepository.save(vehiculo);

        return mantenimiento;
    }
}
