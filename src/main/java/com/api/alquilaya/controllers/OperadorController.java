package com.api.alquilaya.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("/api/operador")
public class OperadorController {
    

    @GetMapping("/prueba")
    public String prueba(){
        return "Acceso de operador autorizado";
    }
}
