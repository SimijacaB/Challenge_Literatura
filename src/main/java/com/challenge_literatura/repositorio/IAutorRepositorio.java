package com.challenge_literatura.repositorio;

import com.challenge_literatura.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IAutorRepositorio extends JpaRepository<Autor, Long>{
    Optional<Autor> findByNombre(String nombre);


    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :anio AND (a.fechaDeFallecimiento IS NULL OR a.fechaDeFallecimiento > :anio)")
    List<Autor> listaAutoresVivosPorAnio(@Param("anio") int anio);
}
