package com.api.alquilaya.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.alquilaya.models.MantenimientoModel;

public interface IMantenimientoRepository extends JpaRepository<MantenimientoModel, Long>{
    List<MantenimientoModel> findByVehiculo_IdVehiculo(Long idVehiculo);
}
