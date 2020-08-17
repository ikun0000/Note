package com.example.repository;

import com.example.entity.Movie;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends Neo4jRepository<Movie, Long> {
    List<Movie> findAllByRolesActorName(String name);
    List<Movie> findByTitle(String title);
}
