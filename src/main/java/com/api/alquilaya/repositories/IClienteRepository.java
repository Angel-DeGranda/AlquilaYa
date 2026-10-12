package com.api.alquilaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.ClienteModel;

@Repository
public interface IClienteRepository extends JpaRepository<ClienteModel, Long> {
    boolean existsByEmail(String email);
    boolean existsByLicenciaConducir(String licenciaConducir);
}
