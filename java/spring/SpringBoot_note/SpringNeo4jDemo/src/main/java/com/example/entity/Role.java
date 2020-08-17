package com.example.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

import java.util.Objects;

@RelationshipEntity(type = "ACTION_IN")
@Data
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    @StartNode
    private Actor actor;

    private String role;

    @EndNode
    private Movie movie;

    public Role(Actor actor, String role, Movie movie) {
        this.actor = actor;
        this.role = role;
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role1 = (Role) o;
        return Objects.equals(id, role1.id) &&
                Objects.equals(actor, role1.actor) &&
                Objects.equals(role, role1.role) &&
                Objects.equals(movie, role1.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, actor, role, movie);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", actor=" + actor +
                ", role='" + role + '\'' +
                ", movie=" + movie +
                '}';
    }
}
