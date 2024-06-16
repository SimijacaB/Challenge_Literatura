package com.challenge_literatura.principal;

import com.challenge_literatura.modelo.*;
import com.challenge_literatura.repositorio.IAutorRepositorio;
import com.challenge_literatura.repositorio.ILibroRepositorio;
import com.challenge_literatura.service.ConsumoApi;
import com.challenge_literatura.service.ConvierteDatosImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leer = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatosImpl conversor = new ConvierteDatosImpl();

    private IAutorRepositorio autorRepositorio;
    private ILibroRepositorio libroRepositorio;
    private List<Autor> autores;
    private List<Libro> libros;

    public Principal(IAutorRepositorio autorRepositorio, ILibroRepositorio libroRepositorio) {
        this.autorRepositorio = autorRepositorio;
        this.libroRepositorio = libroRepositorio;
    }

    public void mostrarMenu() {
        var opcion = -1;

        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por título
                    2 - Mostrar libros 
                    3 - Mostrar autores 
                    4 - Autores vivos en determinado año
                    5 - Buscar libros por idioma
                    6 - Top 10 libros más descargados
                    7 - Libro más descargado y menos descargado        
                    0 - Salir
                                        
                    Ingresa una opción:
                    """;
            System.out.println(menu);

            try {
                opcion = leer.nextInt();
                leer.nextLine(); // consume la nueva línea
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número válido.");
                leer.next(); // descarta la entrada incorrecta
                continue;
            }

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarTodosLosLibros();
                    break;
                case 3:
                    mostrarTodosLosAutores();
                    break;
                case 4:
                    autoresVivosFechaDeterminada();
                    break;
                case 5:
                    buscarLibrosPorIdioma();
                    break;
                case 6:
                    top10LibrosMasDescargados();
                    break;
                case 7:
                    libroMasDescargadoYMenosDescargado();
                    break;
                case 0:
                    System.out.println("Cerrando menú ... ");
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }

        }

    }


    private ResultadosBusqueda realizarBusqueda() {
        System.out.println("Escribe el nombre del libro: ");
        var nombreLibro = leer.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
        ResultadosBusqueda datos = conversor.obtenerDatos(json, ResultadosBusqueda.class);

        // Filtrar los resultados para incluir solo los libros cuyo título contiene el término de búsqueda
        List<DatosLibro> librosFiltrados = datos.resultado().stream()
                .filter(libro -> libro.titulo().toLowerCase().contains(nombreLibro.toLowerCase()))
                .collect(Collectors.toList());

        // Crear un nuevo objeto ResultadosBusqueda con los libros filtrados
        ResultadosBusqueda datosFiltrados = new ResultadosBusqueda(librosFiltrados.size(), librosFiltrados);

        return datosFiltrados;
    }

    private void buscarLibroPorTitulo() {
        ResultadosBusqueda datosBusqueda = realizarBusqueda();
        if (datosBusqueda != null && !datosBusqueda.resultado().isEmpty()) {
            DatosLibro primerLibro = datosBusqueda.resultado().get(0);
            Libro libro = new Libro(primerLibro);
            System.out.println(libro);

            Optional<Libro> libroExiste = libroRepositorio.findByTitulo(libro.getTitulo());
            if (libroExiste.isPresent()) {
                System.out.println("\nEl libro ya esta registrado\n");
            } else {

                if (!primerLibro.autores().isEmpty()) {
                    DatosAutor autor = primerLibro.autores().get(0);
                    Autor autor1 = new Autor(autor);
                    Optional<Autor> autorOptional = autorRepositorio.findByNombre(autor1.getNombre());

                    if (autorOptional.isPresent()) {
                        Autor autorExiste = autorOptional.get();
                        libro.setAutor(autorExiste);
                        libroRepositorio.save(libro);
                    } else {
                        Autor autorNuevo = autorRepositorio.save(autor1);
                        libro.setAutor(autorNuevo);
                        libroRepositorio.save(libro);
                    }

                    Integer numeroDescargas = libro.getNumeroDescargas() != null ? libro.getNumeroDescargas() : 0;
                    var salida = """
                            Libro registrado con éxito
                                                        Titulo: %s
                                                        Autor: %s
                                                        Idioma: %s
                                                        Numero de Descargas: %s
                                                        """;
                    System.out.printf(salida, libro.getTitulo(), autor1.getNombre(), libro.getIdiomas(), numeroDescargas);

                } else {
                    System.out.println("Sin autor");
                }
            }
        } else {
            System.out.println("libro no encontrado");
        }
    }

    private void mostrarTodosLosLibros() {
        libros = libroRepositorio.findAll();
        libros.stream()
                .forEach(System.out::println);
    }

    private void mostrarTodosLosAutores() {
        autores = autorRepositorio.findAll();
        autores.stream()
                .forEach(System.out::println);
    }

    private void autoresVivosFechaDeterminada() {
        System.out.println("Ingresa el año vivo de autor(es) que desea buscar: ");
        var anio = leer.nextInt();
        leer.nextLine(); // consume la nueva línea
        autores = autorRepositorio.listaAutoresVivosPorAnio(anio);
        autores.stream()
                .forEach(System.out::println);
    }

    private List<Libro> datosBusquedaIdioma(String idioma) {
        var dato = Idioma.fromString(idioma);
        System.out.println("Lenguaje buscado: " + dato);

        List<Libro> libroPorIdioma = libroRepositorio.findByIdiomas(dato);
        return libroPorIdioma;
    }

    private void buscarLibrosPorIdioma() {
        System.out.println("Selecciona el idioma que deseas buscar: ");

        Map<Integer, String> opcionesIdioma = new HashMap<>();
        opcionesIdioma.put(1, "en");
        opcionesIdioma.put(2, "es");
        opcionesIdioma.put(3, "fr");
        opcionesIdioma.put(4, "pt");
        opcionesIdioma.put(5, "it");
        opcionesIdioma.put(6, "de");
        opcionesIdioma.put(7, "ru");
        opcionesIdioma.put(8, "zh");
        opcionesIdioma.put(9, "ja");

        var opcion = -1;
        while (opcion != 0) {
            var opciones = """
                    1. en - Ingles
                    2. es - Español
                    3. fr - Francés
                    4. pt - Portugués
                    5. it - Italiano
                    6. de - Alemán
                    7. ru - Ruso
                    8. zh - Chino
                    9. ja - Japonés

                    0. Salir
                    """;
            System.out.println(opciones);
            while (!leer.hasNextInt()) {
                System.out.println("Ingrese una opcion correcta");
                leer.nextLine();
            }
            opcion = leer.nextInt();
            leer.nextLine(); // consume la nueva línea

            if (opcion == 0) {
                break; // salir del bucle
            }

            String idioma = opcionesIdioma.get(opcion);
            if (idioma != null) {
                List<Libro> librosPorIdioma = datosBusquedaIdioma(idioma);
                librosPorIdioma.forEach(System.out::println);
            } else {
                System.out.println("Ningún idioma seleccionado");
            }
        }
    }

    private void top10LibrosMasDescargados() {
        Pageable topTen = PageRequest.of(0, 10);
        List<Libro> topLibros = libroRepositorio.top10LibrosMasDescargados(topTen);
        topLibros.forEach(System.out::println);
    }

    private void libroMasDescargadoYMenosDescargado() {
        libros = libroRepositorio.findAll();
        IntSummaryStatistics est = libros.stream()
                .filter(l -> l.getNumeroDescargas() > 0)
                .collect(Collectors.summarizingInt(Libro::getNumeroDescargas));

        Libro libroMasDescargado = libros.stream()
                .filter(l -> l.getNumeroDescargas() == est.getMax())
                .findFirst()
                .orElse(null);

        Libro libroMenosDescargado = libros.stream()
                .filter(l -> l.getNumeroDescargas() == est.getMin())
                .findFirst()
                .orElse(null);

        var salida = """
                Libro más descargado: %s
                Número de descargas: %d
                                
                Libro menos descargado: %s
                Número de descargas: %d
                """;
        System.out.printf(salida, libroMasDescargado, est.getMax(), libroMenosDescargado, est.getMin());
    }
}