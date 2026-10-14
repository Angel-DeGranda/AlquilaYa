package com.api.alquilaya.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.repositories.IAlquilerRepository;
import com.api.alquilaya.repositories.IClienteRepository;
import com.api.alquilaya.repositories.IVehiculoRepository;

import jakarta.transaction.Transactional;

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

    public AlquilerModel buscarPorId(Long id) {
        return alquilerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Alquiler no encontrado con id " + id));
    }

    @Transactional
    public AlquilerModel crear(Long idVehiculo, Long idCliente, LocalDate fechaInicio, LocalDate fechaFin) {
        if (!fechaFin.isAfter(fechaInicio))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La fecha de fin debe ser posterior a la fecha de inicio");

        VehiculoModel vehiculo = vehiculoRepository.findById(idVehiculo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Vehículo no encontrado con id " + idVehiculo));
        ClienteModel cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con id " + idCliente));

        if (cliente.getEstatus() != ClienteModel.ClienteStatus.Activo)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente no está activo");
        if (!vehiculo.estaDisponible())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El vehículo no está disponible");

        int dias = (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        BigDecimal precioTotal = vehiculo.calcularPrecioAlquiler(dias); // polimórfico

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

    @Transactional
    public AlquilerModel completar(Long idAlquiler) {
        AlquilerModel alquiler = buscarPorId(idAlquiler);

        if (alquiler.getEstatus() == AlquilerModel.AlquilerStatus.Completado)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El alquiler ya fue completado");

        alquiler.setEstatus(AlquilerModel.AlquilerStatus.Completado);
        alquilerRepository.save(alquiler);

        VehiculoModel vehiculo = alquiler.getVehiculo();
        vehiculo.devolver();
        vehiculoRepository.save(vehiculo);

        return alquiler;
    }
}
