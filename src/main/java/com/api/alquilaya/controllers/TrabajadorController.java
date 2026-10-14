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

import com.api.alquilaya.models.TrabajadorModel;
import com.api.alquilaya.services.TrabajadorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {
    private final TrabajadorService trabajadorService;

    public TrabajadorController(TrabajadorService trabajadorService) {
        this.trabajadorService = trabajadorService;
    }

    @GetMapping
    public List<TrabajadorModel> listar() {
        return trabajadorService.listarTodos();
    }

    @GetMapping("/{id}")
    public TrabajadorModel obtener(@PathVariable Long id) {
        return trabajadorService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<TrabajadorModel> crear(@Valid @RequestBody TrabajadorModel trabajador) {
        TrabajadorModel creado = trabajadorService.crear(trabajador);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getIdTrabajador()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrabajadorModel> actualizar(@PathVariable Long id,
            @Valid @RequestBody TrabajadorModel trabajador) {
        return ResponseEntity.ok(trabajadorService.actualizar(id, trabajador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        trabajadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}