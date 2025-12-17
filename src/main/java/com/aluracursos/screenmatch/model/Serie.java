package com.aluracursos.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.OptionalDouble;

public class Serie {
    private String titulo;
    private Integer totalTemporadas;
    private Double evaluacion;
    private String poster;
    private Categoria genero;
    private String actores;
    private String sinopsis;

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
        this.sinopsis = datosSerie.sinopsis();
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
