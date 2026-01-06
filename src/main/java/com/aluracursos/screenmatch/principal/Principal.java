package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    //ATRIBUTOS
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String OMDB_API_KEY = System.getenv("OMDB_API_KEY");
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    //CONSTRUCTOR
    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    //METODOS
    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    ---- SCREENMATCH ----
                    
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar serie por titulo
                    5 - Top 5 series
                    6 - Buscar series por categoria
                    7 - Buscar series por temporadas y evaluacion
                    8 - Buscar episodio por nombre
                    9 - Top 5 episodios por serie
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarPorCategoria();
                    break;
                case 7:
                    buscarPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodioPorNombre();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + "&apikey=" + OMDB_API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarEpisodioPorSerie() {
        System.out.println("---LISTA DE SERIES EN LA BASE DE DATOS---");
        mostrarSeriesBuscadas();
        System.out.println("-------------------------------------------------------------");
        System.out.println("Escribe el nombre de la serie que quieres ver los episodios: ");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findAny();

        if (serie.isPresent()){
            var serieEncontrada = serie.get();

            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + "&apikey=" + OMDB_API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }

            List<Episodio> episodios = temporadas.stream()
                            .flatMap(dt -> dt.episodios().stream()
                                    .map(de -> new Episodio(dt.numero(), de)))
                            .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

            episodios.forEach(System.out::println);
            System.out.println("");
        }
    }
    
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        datosSeries.add(datos);
        // Convertimos los DatosSerie (record) a nuestra Entidad Serie
        Serie serie = new Serie(datos);
        // ¡Aquí ocurre la magia! Guardamos en la base de datos
        repositorio.save(serie);
        System.out.println(datos);
        System.out.println("\n");
    }

    private void mostrarSeriesBuscadas(){
        // Ya no usamos la lista en memoria.
        // Usamos el repositorio para buscar en la base de datos real.
        series = repositorio.findAll();

        // Ordenar por género e imprimir
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
        System.out.println("\n");
    }

    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que quieres ver: ");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("La serie es: ");
            System.out.println(serieBuscada.get());
        }else {
            System.out.println("Serie no encontrada");
        }
    }

    private void buscarTop5Series(){
        series = repositorio.findTop5ByOrderByEvaluacionDesc();
        series.forEach(s -> System.out.println("Titulo: " + s.getTitulo() + ", Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarPorCategoria() {
        System.out.println("Escribe el nombre de la categoria: ");
        var nombreCategoria = teclado.nextLine();
        var cateogoriaEcontrada = Categoria.fromEspanol(nombreCategoria);
        series = repositorio.findByGenero(cateogoriaEcontrada);
        System.out.println("Series de la categoria " + nombreCategoria + " Son: ");
        series.forEach(System.out::println);
    }

    private void buscarPorTemporadaYEvaluacion(){
        System.out.println("Ingresa el maximo de temporadas que debe tener la serie: ");
        var numeroMaximoDeTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Ingresa la evaluacion minima que debe tener la serie: ");
        var minimoDeEvaluacion = teclado.nextDouble();
        teclado.nextLine();
        series = repositorio.seriesPorTemporadaYEvaluacion(numeroMaximoDeTemporadas, minimoDeEvaluacion);
        series.forEach(s -> System.out.println("Titulo: " + s.getTitulo() + "; Temporadas: " + s.getTotalTemporadas() + "; Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodioPorNombre(){
        System.out.println("Ingresa el nombre del episodio a buscar: ");
        var nombreDelEpisodio = teclado.nextLine();
        Optional<Episodio> episodioBuscado = repositorio.episodiosPorNombre(nombreDelEpisodio);
        if (episodioBuscado.isPresent()){
            System.out.printf("Serie: %s ; Temporada: %d ; Episodio: %d ; Evaluacion: %.2f", episodioBuscado.get().getSerie().getTitulo(), episodioBuscado.get().getTemporada(), episodioBuscado.get().getNumeroEpisodio(), episodioBuscado.get().getEvaluacion());
        }else {
            System.out.println("Episodio " + nombreDelEpisodio  + " no encontrado");
        }
    }

    private void buscarTop5Episodios(){
        buscarSeriesPorTitulo();
        Serie serieEncontrada = serieBuscada.get();
        List<Episodio> top5Episodios = repositorio.top5Episodios(serieEncontrada);
        top5Episodios.forEach(e -> System.out.printf("Serie: %s ; Temporada: %d ; Episodio: %d ; Evaluacion: %.2f %n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
    }
}

