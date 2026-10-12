package com.api.alquilaya.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.api.alquilaya.models.TrabajadorModel;


@Repository 
public interface ITrabajadorRepository extends JpaRepository<TrabajadorModel, Long> {
    boolean existsByEmail(String email);
    boolean existsByLicenciaConducir(String licenciaConducir);

    List<TrabajadorModel> findByOficina_IdOficina(Long idOficina);
    
}