package com.example.studentmanagement.repository;

import com.example.studentmanagement.dto.StatistiqueAnnee;
import com.example.studentmanagement.entity.Filiere;
import com.example.studentmanagement.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Vérifie les requêtes réellement exécutées par Hibernate. @DataJpaTest remplace MySQL par H2 en mémoire
 * et annule chaque test à la fin (rollback) : les tests ne se gênent pas entre eux.
 */
@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FiliereRepository filiereRepository;

    @BeforeEach
    void remplir() {
        Filiere gi = filiereRepository.save(new Filiere("GI", "Génie informatique"));
        Filiere rt = filiereRepository.save(new Filiere("RT", "Réseaux"));
        etudiant("D100000001", "Bennani", 2002, gi);
        etudiant("D100000002", "Alaoui", 2002, gi);
        etudiant("D100000003", "Benjelloun", 2003, rt);
        etudiant("D100000004", "Tazi", 2001, null);
    }

    @Test
    void nombreDEtudiantsParAnneeDeNaissanceTrieParAnnee() {
        assertThat(studentRepository.findNbrStudentByYear()).containsExactly(
                new StatistiqueAnnee(2001, 1),
                new StatistiqueAnnee(2002, 2),
                new StatistiqueAnnee(2003, 1));
    }

    @Test
    void etudiantsDUneFiliereTriesParNom() {
        List<Student> gi = studentRepository.findByFiliereCodeFiliereOrderByNomFamille("GI");

        assertThat(gi).extracting(Student::getNomFamille).containsExactly("Alaoui", "Bennani");
    }

    @Test
    void rechercheParNomPartielleEtSansMajuscules() {
        assertThat(studentRepository.findByNomFamilleContainingIgnoreCaseOrderByNomFamille("BEN"))
                .extracting(Student::getNomFamille).containsExactly("Benjelloun", "Bennani");
    }

    @Test
    void unCneNePeutPasEtreUtiliseDeuxFois() {
        assertThatThrownBy(() -> etudiant("D100000001", "Doublon", 2000, null))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    // saveAndFlush : l'INSERT part tout de suite, la violation de contrainte unique est donc levée ici
    private void etudiant(String cne, String nom, int anneeNaissance, Filiere filiere) {
        Student student = new Student(cne, nom, "Prénom", LocalDate.of(anneeNaissance, 6, 15));
        student.setFiliere(filiere);
        studentRepository.saveAndFlush(student);
    }
}
