package com.api.alquilaya.dto;

import java.time.LocalDate;

public record AlquilerRequest(Long idVehiculo, Long idCliente, LocalDate fechaInicio, LocalDate fechaFin) {

}
