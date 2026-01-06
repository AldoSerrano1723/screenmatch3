package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

// <Serie, Long> indica: <Entidad a manejar, Tipo de dato del ID>
public interface SerieRepository extends JpaRepository<Serie, Long> { // ¡Listo! Al extender de JpaRepository, ya tenemos métodos como save(), findAll(), etc.

    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(Integer totalTemporadas, Double evaluacion);

    @Query("select e from Serie s join s.episodios e where e.titulo ilike %:nombreDelEpisodio%")
    Optional<Episodio> episodiosPorNombre(String nombreDelEpisodio);

    @Query("select e from Serie s join s.episodios e where s = :serie order by e.evaluacion desc limit 5")
    List<Episodio> top5Episodios(Serie serie);
}
