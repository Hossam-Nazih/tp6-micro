package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.Filiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiliereRepository extends JpaRepository<Filiere, Integer> {

    Optional<Filiere> findByCodeFiliere(String codeFiliere);
}
