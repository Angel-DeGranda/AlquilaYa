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

import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.services.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public List<ClienteModel> listar() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ClienteModel obtener(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ClienteModel> crear(@Valid @RequestBody ClienteModel cliente) {
        ClienteModel creado = clienteService.crear(cliente);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.getIdCliente()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteModel> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteModel cliente) {
        return ResponseEntity.ok(clienteService.actualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
