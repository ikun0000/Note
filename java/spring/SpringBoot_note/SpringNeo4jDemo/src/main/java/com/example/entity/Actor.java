package com.example.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NodeEntity(label = "Actor")
@Data
public class Actor {

    @Id
    @GeneratedValue
    private Long id;
    @Properties
    private String name;

    @Relationship(type = "ACTION_IN")
    private List<Role> roles = new ArrayList<>();

    public Actor(String name) {
        this.name = name;
    }

    public void actionIn(Movie movie, String roleName) {
        Role role = new Role(this, roleName, movie);
        roles.add(role);
        movie.getRoles().add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Actor actor = (Actor) o;
        return Objects.equals(id, actor.id) &&
                Objects.equals(name, actor.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
