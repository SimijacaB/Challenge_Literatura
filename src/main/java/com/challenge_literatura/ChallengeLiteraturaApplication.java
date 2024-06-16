package com.challenge_literatura;

import com.challenge_literatura.principal.Principal;
import com.challenge_literatura.repositorio.IAutorRepositorio;
import com.challenge_literatura.repositorio.ILibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraturaApplication implements CommandLineRunner {
    @Autowired
    private ILibroRepositorio libroRepositorio;
    @Autowired
    private IAutorRepositorio autorRepositorio;

    public static void main(String[] args)  {
        SpringApplication.run(ChallengeLiteraturaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(autorRepositorio, libroRepositorio);
        principal.mostrarMenu();
    }
}
