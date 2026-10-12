package com.api.alquilaya.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MantenimientoRequest( Long idVehiculo, Long idTrabajadorResponsable, Long idOficinaResponsable, LocalDate fechaInicioMantenimiento, String descripcion, BigDecimal costo, Long kilometraje ) {
    
}
