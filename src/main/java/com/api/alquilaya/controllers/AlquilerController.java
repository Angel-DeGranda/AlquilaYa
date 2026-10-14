package com.api.alquilaya.controllers;

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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.alquilaya.models.AlquilerModel;
import com.api.alquilaya.services.AlquilerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

     // Datos de entrada del POST. Va aquí dentro para no necesitar el paquete dto.
    public record AlquilerRequest(
            @NotNull Long idVehiculo,
            @NotNull Long idCliente,
            @NotNull @FutureOrPresent LocalDate fechaInicio,
            @NotNull LocalDate fechaFin) {
    }

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) {
        this.alquilerService = alquilerService;
    }

    @GetMapping
    public List<AlquilerModel> listar() {
        return alquilerService.listarTodos();
    }

    @GetMapping("/{id}")
    public AlquilerModel obtener(@PathVariable Long id) {
        return alquilerService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<AlquilerModel> crear(@Valid @RequestBody AlquilerRequest request) {
        AlquilerModel creado = alquilerService.crear(request.idVehiculo(), request.idCliente(),
                request.fechaInicio(), request.fechaFin());

        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getIdAlquiler()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}/completar")
    public AlquilerModel completar(@PathVariable Long id) {
        return alquilerService.completar(id);
    }
}
