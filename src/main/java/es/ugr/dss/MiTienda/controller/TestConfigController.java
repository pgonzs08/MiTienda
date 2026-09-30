package es.ugr.dss.MiTienda.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestConfigController {

    // Lee la propiedad del archivo application.properties
    @Value("${spring.application.name:No encontrada}")
    private String appName;

    @GetMapping("/test-config")
    public String checkConfig() {
        return "El nombre de la aplicación es: " + appName;
    }
}