package com.challenge_literatura.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "autores")
@NoArgsConstructor
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    @Column(name = "fecha_fallecimiento")
    private String fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libro;

    public Autor(DatosAutor datosAutor) {
        String nombreCompleto = datosAutor.nombre();
        String[] partes = nombreCompleto.split(", ");
        if (partes.length == 2) {
            this.nombre = partes[1] + " " + partes[0];
        } else {
            this.nombre = nombreCompleto;
        }
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
    }

    @Override
    public String toString() {
        return String.format(
                "Id: %d\nNombre: %s\nFecha de nacimiento: %s\nFecha de fallecimiento: %s\n",
                id,
                nombre,
                fechaNacimiento,
                fechaDeFallecimiento
        );
    }
}
