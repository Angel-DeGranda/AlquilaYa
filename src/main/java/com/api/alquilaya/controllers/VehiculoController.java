package com.api.alquilaya.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.VehiculoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoModel> listar() {
        return vehiculoService.listarTodos();
    }

    @GetMapping("/disponibles")
    public List<VehiculoModel> listarDisponibles() {
        return vehiculoService.listarDisponibles();
    }

    @GetMapping("/{id}")
    public VehiculoModel obtener(@PathVariable Long id) {
        return vehiculoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<VehiculoModel> crear(@Valid @RequestBody VehiculoModel vehiculo) {
        VehiculoModel creado = vehiculoService.crear(vehiculo);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getIdVehiculo()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehiculoModel> actualizar(@PathVariable Long id,
            @Valid @RequestBody VehiculoModel vehiculo) {
        return ResponseEntity.ok(vehiculoService.actualizar(id, vehiculo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
