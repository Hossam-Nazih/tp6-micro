package com.example.studentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée. @SpringBootApplication active la configuration automatique (datasource, JPA, Tomcat...)
 * et l'analyse des composants : tout ce qui est dans ce package et ses sous-packages est détecté.
 */
@SpringBootApplication
public class StudentManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudentManagementApplication.class, args);
    }
}
