package com.api.alquilaya.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.alquilaya.models.OficinaAlquilerModel;

public interface IOficinaAlquilerRepository extends JpaRepository<OficinaAlquilerModel, Long> {
    
}
