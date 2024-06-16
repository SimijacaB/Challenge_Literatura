package com.challenge_literatura.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@Entity
@Table(name = "libros")
@NoArgsConstructor

public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    @ManyToOne
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private Idioma idiomas;

    @Column(name = "numero_descargas")
    private Integer numeroDescargas;

    public Libro(DatosLibro datosLibro) {
        this.id = datosLibro.id();
        this.titulo = datosLibro.titulo();
        this.idiomas = Idioma.fromString(datosLibro.idiomas().get(0));
        this.numeroDescargas = datosLibro.numeroDescargas();
    }

    @Override
    public String toString() {
        return String.format(
                "Id: %d\nTitulo: %s\nAutor: %s\nIdioma: %s\nNumero de descargas: %d\n",
                id,
                titulo,
                autor != null ? autor.getNombre() : "N/A",
                idiomas,
                numeroDescargas
        );
    }
}
