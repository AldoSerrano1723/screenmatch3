package com.aluracursos.screenmatch.model;

import com.aluracursos.screenmatch.service.ConsultaGemini;
import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    //ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;

    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String actores;
    private String sinopsis;

    // RELACIÓN: Una serie tiene una lista de episodios
    // 'mappedBy' debe coincidir con el nombre del atributo en la clase Episodio ("serie")
    @OneToMany(mappedBy = "serie")
    private List<Episodio> episodios;

    //CONSTRUCTORES
    public Serie(DatosSerie datosSerie) {
        this.titulo = datosSerie.titulo();
        this.totalTemporadas = datosSerie.totalTemporadas();

        // Transformación de Evaluación
        // Usando OptionalDouble para la conversión
        // 1. Double.valueOf convierte el String a Double
        // 2. OptionalDouble.of envuelve ese valor
        // 3. orElse(0.0) devuelve el valor o 0.0 si la caja estuviera vacía
        this.evaluacion = OptionalDouble.of(Double.valueOf(datosSerie.evaluacion())).orElse(0.0);

        this.poster = datosSerie.poster();

        // Transformación de Género
        this.genero = Categoria.fromString(datosSerie.genero().split(",")[0].trim());

        this.actores = datosSerie.actores();
        this.sinopsis = ConsultaGemini.obtenerTraduccion(datosSerie.sinopsis());
    }

    public Serie(){}

    //GETTERS Y SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    //METODOS
    @Override
    public String toString() {
        return """
         
            ----- FICHA TÉCNICA -----
            Título: %s
            Género: %s
            Temporadas: %d
            Evaluación: %.1f
            Actores: %s
            Sinopsis: %s
            -------------------------
            """.formatted(titulo, genero, totalTemporadas, evaluacion, actores, sinopsis);
    }
}
