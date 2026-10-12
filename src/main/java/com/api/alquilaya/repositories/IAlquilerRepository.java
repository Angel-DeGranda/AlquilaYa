package com.api.alquilaya.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.alquilaya.models.AlquilerModel;

public interface IAlquilerRepository extends JpaRepository<AlquilerModel, Long> {
    List<AlquilerModel> findByEstatus(AlquilerModel.AlquilerStatus eStatus);
    List<AlquilerModel> findByVehiculo_IdVehiculo(Long idVehidulo);
    List<AlquilerModel> findByCliente_IdCliente(Long idCliente);
}
