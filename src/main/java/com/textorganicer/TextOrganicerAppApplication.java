package com.textorganicer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TextOrganicerAppApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TextOrganicerAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n---------        Bienvenido a la API de textOrganicer       ---------\n\n          DOC: http://localhost:8080/swagger-ui/#/\n");
    }
}
