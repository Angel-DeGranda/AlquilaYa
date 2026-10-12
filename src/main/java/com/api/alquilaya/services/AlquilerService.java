package com.api.alquilaya.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.api.alquilaya.exception.RecursoNoEncontradoException;
import com.api.alquilaya.exception.ReglaNegocioException;
import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IAlquilerRepository;
import com.api.alquilaya.repositories.IClienteRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;

@Service
public class AlquilerService {

    private final IAlquilerRepository alquilerRepository;
    private final IVehiculoRepository vehiculoRepository;
    private final IClienteRepository clienteRepository;

    public AlquilerService(IAlquilerRepository alquilerRepository,
            IVehiculoRepository vehiculoRepository,
            IClienteRepository clienteRepository) {
        this.alquilerRepository = alquilerRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<AlquilerModel> listarTodos() {
        return alquilerRepository.findAll();
    }

    public AlquilerModel crear(Long idVehiculo, Long idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        VehiculoModel vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con id " + idVehiculo));
        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con id " + idCliente));

        if (!vehiculo.estaDisponible()) {
            throw new ReglaNegocioException("El vehículo no está disponible");
        }

        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        BigDecimal precioTotal = vehiculo.calcularPrecioAlquiler(dias);

        AlquilerModel alquiler = new AlquilerModel();
        alquiler.setVehiculo(vehiculo);
        alquiler.setCliente(cliente);
        alquiler.setFechaInicioAlquiler(fechaInicio);
        alquiler.setFechaFinAlquiler(fechaFin);
        alquiler.setPrecioTotal(precioTotal);
        alquiler.setEstatus(AlquilerModel.AlquilerStatus.Pendiente);

        vehiculo.alquilar();
        vehiculoRepository.save(vehiculo);

        return alquilerRepository.save(alquiler);
    }

    public AlquilerModel buscarPorId(Long id) {
        return alquilerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alquiler no encontrado con id " + id));
    }

    public AlquilerModel completar(Long idAlquiler) {
        AlquilerModel alquiler = alquilerRepository.findById(idAlquiler)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alquiler no encontrado con id " + idAlquiler));

        alquiler.setEstatus(AlquilerModel.AlquilerStatus.Completado);
        alquilerRepository.save(alquiler);

        VehiculoModel vehiculo = alquiler.getVehiculo();
        vehiculo.devolver();
        vehiculoRepository.save(vehiculo);

        return alquiler;
    }
}
