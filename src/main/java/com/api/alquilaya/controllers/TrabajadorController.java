package com.api.alquilaya.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.alquilaya.models.TrabajadorModel;
import com.api.alquilaya.services.TrabajadorService;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {
    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    @GetMapping
    public List<TrabajadorModel> listar() { return trabajadorService.listarTodos(); }

    @GetMapping("/{id}")
    public TrabajadorModel obtener(@PathVariable Long id) { return trabajadorService.buscarPorId(id); }

    @PostMapping
    public TrabajadorModel crear(@RequestBody TrabajadorModel trabajador) { return trabajadorService.crear(trabajador); }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { trabajadorService.eliminar(id); }
}