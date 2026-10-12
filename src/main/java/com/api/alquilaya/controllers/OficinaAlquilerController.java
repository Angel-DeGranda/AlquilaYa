package com.api.alquilaya.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.services.OficinaAlquilerService;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaAlquilerController {
    private final OficinaAlquilerService oficinaService;

    public OficinaAlquilerController(OficinaAlquilerService oficinaService) {
        this.oficinaService = oficinaService;
    }

    @GetMapping
    public List<OficinaAlquilerModel> listar() { return oficinaService.listarTodas(); }

    @GetMapping("/{id}")
    public OficinaAlquilerModel obtener(@PathVariable Long id) { return oficinaService.buscarPorId(id); }

    @PostMapping
    public OficinaAlquilerModel crear(@RequestBody OficinaAlquilerModel oficina) { return oficinaService.guardar(oficina); }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) { oficinaService.eliminar(id); }
}
