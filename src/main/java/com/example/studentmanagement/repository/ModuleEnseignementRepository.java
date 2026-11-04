package com.example.studentmanagement.repository;

import com.example.studentmanagement.entity.ModuleEnseignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleEnseignementRepository extends JpaRepository<ModuleEnseignement, Integer> {

    List<ModuleEnseignement> findByFiliereCodeFiliereOrderBySemestreAscCodeModuleAsc(String codeFiliere);
}
