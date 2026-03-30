package com.Logistica.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogisticaController {

    @GetMapping("/saludo")
    public String holaLogistica() {
        return "¡Hola! El microservicio de Logística está funcionando correctamente.";
    }
}