package com.textorganicer.controlador;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controlTest {

    @GetMapping
    public String test() {
        return "Bienvenido a la API REST de textOrganicer";
    }


}
