package com.api.alquilaya.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.alquilaya.models.VehiculoModel;
import com.api.alquilaya.services.VehiculoService;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {
    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<VehiculoModel> listar() { return vehiculoService.listarTodos(); }

    @GetMapping("/disponibles")
    public List<VehiculoModel> listarDisponibles() { return vehiculoService.listarDisponibles(); }

    @GetMapping("/{id}")
    public VehiculoModel obtener(@PathVariable Long id) { return vehiculoService.buscarPorId(id); }

    @PostMapping
    public VehiculoModel crear(@RequestBody VehiculoModel vehiculo) { return vehiculoService.crear(vehiculo); }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { vehiculoService.eliminar(id); }
}
