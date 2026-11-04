package com.example.studentmanagement.repository;

import com.example.studentmanagement.dto.StatistiqueAnnee;
import com.example.studentmanagement.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data génère l'implémentation au démarrage : save, findAll, findById, count, delete... sont déjà là.
 * Seules les recherches propres au métier sont déclarées ici.
 *
 * Pas de "Student findById(int id)" comme dans l'énoncé : JpaRepository fournit déjà findById(Integer),
 * qui renvoie un Optional et oblige ainsi l'appelant à traiter le cas "étudiant introuvable".
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    /** Nombre d'étudiants par année de naissance, de la plus ancienne à la plus récente. */
    @Query("select new com.example.studentmanagement.dto.StatistiqueAnnee(year(s.dateDeNaissance), count(s))"
            + " from Student s group by year(s.dateDeNaissance) order by year(s.dateDeNaissance)")
    List<StatistiqueAnnee> findNbrStudentByYear();

    // Requêtes dérivées : Spring Data écrit le JPQL à partir du nom de la méthode
    List<Student> findByFiliereCodeFiliereOrderByNomFamille(String codeFiliere);

    List<Student> findByNomFamilleContainingIgnoreCaseOrderByNomFamille(String morceauDuNom);
}
