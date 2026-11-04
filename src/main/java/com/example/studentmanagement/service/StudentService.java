package com.example.studentmanagement.service;

import com.example.studentmanagement.dto.StatistiqueAnnee;
import com.example.studentmanagement.entity.Filiere;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.RessourceIntrouvableException;
import com.example.studentmanagement.repository.FiliereRepository;
import com.example.studentmanagement.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Logique métier des étudiants, entre le contrôleur (HTTP) et le repository (base).
 *
 * Injection par le constructeur plutôt que @Autowired sur un champ : les dépendances sont final,
 * visibles dans la signature, et un test peut créer le service avec ses propres repositories.
 */
@Service
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final FiliereRepository filiereRepository;

    public StudentService(StudentRepository studentRepository, FiliereRepository filiereRepository) {
        this.studentRepository = studentRepository;
        this.filiereRepository = filiereRepository;
    }

    /**
     * Crée l'étudiant, ou le met à jour s'il a déjà un id (comportement de save() de Spring Data).
     * La filière envoyée en JSON ne contient souvent que son id : on la remplace par la vraie, et on refuse un id inconnu.
     */
    @Transactional
    public Student save(Student student) {
        if (student.getFiliere() != null) {
            student.setFiliere(filiereParId(student.getFiliere().getId()));
        }
        return studentRepository.save(student);
    }

    /** Remplace toutes les données de l'étudiant {@code id} par celles reçues. */
    @Transactional
    public Student update(int id, Student donnees) {
        if (!studentRepository.existsById(id)) {
            throw new RessourceIntrouvableException("Étudiant introuvable : " + id);
        }
        donnees.setId(id);
        return save(donnees);
    }

    /** true si l'étudiant existait et a été supprimé, false s'il n'existait pas. */
    @Transactional
    public boolean delete(int id) {
        if (!studentRepository.existsById(id)) {
            return false;
        }
        studentRepository.deleteById(id);
        return true;
    }

    public Student findById(int id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Étudiant introuvable : " + id));
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public long countStudents() {
        return studentRepository.count();
    }

    public List<StatistiqueAnnee> findNbrStudentByYear() {
        return studentRepository.findNbrStudentByYear();
    }

    public List<Student> findByFiliere(String codeFiliere) {
        return studentRepository.findByFiliereCodeFiliereOrderByNomFamille(codeFiliere);
    }

    public List<Student> rechercherParNom(String morceauDuNom) {
        return studentRepository.findByNomFamilleContainingIgnoreCaseOrderByNomFamille(morceauDuNom);
    }

    private Filiere filiereParId(Integer id) {
        if (id == null) {
            throw new RessourceIntrouvableException("La filière doit être désignée par son id");
        }
        return filiereRepository.findById(id)
                .orElseThrow(() -> new RessourceIntrouvableException("Filière introuvable : " + id));
    }
}
