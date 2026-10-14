package com.api.alquilaya.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.api.alquilaya.models.ClienteModel;
import com.api.alquilaya.repositories.IClienteRepository;

import jakarta.transaction.Transactional;

@Service
public class ClienteService {

    private final IClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(IClienteRepository clienteRepository, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<ClienteModel> listarTodos() {
        return clienteRepository.findAll();
    }

    public ClienteModel buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Cliente no encontrado con id " + id));
    }

    public ClienteModel crear(ClienteModel cliente) {
        if (cliente.getPassword() == null || cliente.getPassword().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        if (clienteRepository.existsByEmail(cliente.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un cliente con ese email");
        if (clienteRepository.existsByLicenciaConducir(cliente.getLicenciaConducir()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un cliente con esa licencia");

        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        return clienteRepository.save(cliente);
    }

    @Transactional
    public ClienteModel actualizar(Long id, ClienteModel datos) {
        ClienteModel cliente = buscarPorId(id);

        if (!cliente.getEmail().equals(datos.getEmail()) && clienteRepository.existsByEmail(datos.getEmail()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un cliente con ese email");
        if (!cliente.getLicenciaConducir().equals(datos.getLicenciaConducir())
                && clienteRepository.existsByLicenciaConducir(datos.getLicenciaConducir()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un cliente con esa licencia");

        cliente.setNombre(datos.getNombre());
        cliente.setApellidos(datos.getApellidos());
        cliente.setEmail(datos.getEmail());
        cliente.setTelefono(datos.getTelefono());
        cliente.setFechaNacimiento(datos.getFechaNacimiento());
        cliente.setLicenciaConducir(datos.getLicenciaConducir());
        cliente.setEstatus(datos.getEstatus());
        if (datos.getPassword() != null && !datos.getPassword().isBlank())
            cliente.setPassword(passwordEncoder.encode(datos.getPassword()));

        return clienteRepository.save(cliente);
    }

    public void eliminar(Long id) {
        if (!clienteRepository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con id " + id);
        clienteRepository.deleteById(id);
    }
}
