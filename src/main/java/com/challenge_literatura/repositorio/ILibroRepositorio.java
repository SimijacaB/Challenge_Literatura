package com.challenge_literatura.repositorio;

import com.challenge_literatura.modelo.Idioma;
import com.challenge_literatura.modelo.Libro;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ILibroRepositorio extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTitulo(String titulo);
    List<Libro> findByIdiomas(Idioma idioma);
    @Query("SELECT l FROM Libro l ORDER BY l.numeroDescargas DESC")
    List<Libro> top10LibrosMasDescargados(Pageable pageable);
}
