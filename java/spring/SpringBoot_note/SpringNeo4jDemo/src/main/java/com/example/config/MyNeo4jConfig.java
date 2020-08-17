package com.example.config;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories("com.example.repository")
@EntityScan(basePackages = "com.example.entity")
public class MyNeo4jConfig {

    @Bean
    org.neo4j.ogm.config.Configuration configuration() {
        org.neo4j.ogm.config.Configuration configuration = new org.neo4j.ogm.config.Configuration.Builder()
                .uri("bolt://10.10.10.246:7687")
                .credentials("neo4j", "123456")
                .build();
        return configuration;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(configuration(), "com.example.entity");
    }

}
