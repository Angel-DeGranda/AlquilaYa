package com.api.alquilaya.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.VehiculoModel;

@Repository
public interface IVehiculoRepository extends JpaRepository<VehiculoModel, Long> {
    boolean existsByMatricula(String matricula);
    List<VehiculoModel> findByEstatus(VehiculoModel.VehiculoStatus estatus);
    List<VehiculoModel> findByOficina_IdOficina(Long idOficina);
}