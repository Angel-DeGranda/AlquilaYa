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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.alquilaya.dto.MantenimientoRequest;
import com.api.alquilaya.models.MantenimientoModel;
import com.api.alquilaya.services.MantenimientoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mantenimientos")
public class MantenimientoController {

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
        MantenimientoModel creado = mantenimientoService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getIdMantenimiento()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}/cerrar")
    public MantenimientoModel cerrar(@PathVariable Long id, @RequestParam LocalDate fechaFin) {
        return mantenimientoService.cerrar(id, fechaFin);
    }
}
