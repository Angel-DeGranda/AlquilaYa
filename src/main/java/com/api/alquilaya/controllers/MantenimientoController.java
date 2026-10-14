package com.api.alquilaya.controllers;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.services.MantenimientoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

    // Datos de entrada del POST. Va aquí dentro para no necesitar el paquete dto.
    public record MantenimientoRequest(
            @NotNull Long idVehiculo,
            @NotNull Long idTrabajadorResponsable,
            Long idOficinaResponsable, // puede ir null: el Service usa la oficina del vehículo
            @NotNull LocalDate fechaInicioMantenimiento,
            @NotBlank @Size(max = 255) String descripcion,
            @NotNull @PositiveOrZero BigDecimal costo,
            @NotNull @PositiveOrZero Long kilometraje) {
    }

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @GetMapping("/vehiculo/{idVehiculo}")
    public List<MantenimientoModel> listarPorVehiculo(@PathVariable Long idVehiculo) {
        return mantenimientoService.listarPorVehiculo(idVehiculo);
    }

    @PostMapping
    public ResponseEntity<MantenimientoModel> crear(@Valid @RequestBody MantenimientoRequest request) {
        MantenimientoModel creado = mantenimientoService.crear(
                request.idVehiculo(),
                request.idTrabajadorResponsable(),
                request.idOficinaResponsable(),
                request.fechaInicioMantenimiento(),
                request.descripcion(),
                request.costo(),
                request.kilometraje());

        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getIdMantenimiento()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}/cerrar")
    public MantenimientoModel cerrar(@PathVariable Long id, @RequestParam LocalDate fechaFin) {
        return mantenimientoService.cerrar(id, fechaFin);
    }
}
