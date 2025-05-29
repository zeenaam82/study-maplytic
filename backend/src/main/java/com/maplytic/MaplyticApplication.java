package com.maplytic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaRepositories("com.maplytic.repository")
public class MaplyticApplication {
    @PostConstruct
    public void printEnv() {
        System.out.println("SPRING_DATASOURCE_URL env: " + System.getenv("SPRING_DATASOURCE_URL"));
        System.out.println("Other envs:");
        System.getenv().forEach((k, v) -> System.out.println(k + " = " + v));
    }
    
    public static void main(String[] args) {
        SpringApplication.run(MaplyticApplication.class, args);
    }

}
