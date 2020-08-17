package com.example.repository;

import com.example.entity.Actor;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends Neo4jRepository<Actor, Long> {
    List<Actor> findAllByRolesMovieTitle(String title);
    List<Actor> findByName(String name);
}
