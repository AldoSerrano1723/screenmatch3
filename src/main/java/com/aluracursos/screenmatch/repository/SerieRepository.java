package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// <Serie, Long> indica: <Entidad a manejar, Tipo de dato del ID>
public interface SerieRepository extends JpaRepository<Serie, Long> { // ¡Listo! Al extender de JpaRepository, ya tenemos métodos como save(), findAll(), etc.

    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
}
