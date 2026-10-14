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

import com.api.alquilaya.models.OficinaAlquilerModel;
import com.api.alquilaya.services.OficinaAlquilerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/oficinas")
public class OficinaAlquilerController {
    private final OficinaAlquilerService oficinaService;

    public OficinaAlquilerController(OficinaAlquilerService oficinaService) {
        this.oficinaService = oficinaService;
    }

    @GetMapping
    public List<OficinaAlquilerModel> listar() {
        return oficinaService.listarTodas();
    }

    @GetMapping("/{id}")
    public OficinaAlquilerModel obtener(@PathVariable Long id) {
        return oficinaService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<OficinaAlquilerModel> crear(@Valid @RequestBody OficinaAlquilerModel oficina) {
        OficinaAlquilerModel creada = oficinaService.guardar(oficina);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creada.getIdOficina()).toUri();
        return ResponseEntity.created(ubicacion).body(creada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OficinaAlquilerModel> actualizar(@PathVariable Long id,
            @Valid @RequestBody OficinaAlquilerModel oficina) {
        return ResponseEntity.ok(oficinaService.actualizar(id, oficina));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        oficinaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
