package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StatistiqueAnnee;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints HTTP des étudiants. Aucune logique métier ici : recevoir la requête, appeler le service,
 * choisir le code HTTP. Les erreurs (404, 400, 409) sont mises en forme par GestionnaireErreurs.
 */
@RestController
@RequestMapping("/students")
@Tag(name = "Étudiants", description = "Inscription, consultation et statistiques des étudiants")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Enregistrer un étudiant",
            description = "Pour l'inscrire dans une filière, ajouter \"filiere\": {\"id\": 1} au JSON.")
    @ApiResponse(responseCode = "201", description = "Étudiant créé, avec son id généré")
    @ApiResponse(responseCode = "400", description = "Champ manquant ou invalide (détail par champ)")
    @ApiResponse(responseCode = "409", description = "CNE ou email déjà utilisé")
    @PostMapping("/save")
    public ResponseEntity<Student> save(@Valid @RequestBody Student student) {
        Student savedStudent = studentService.save(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @Operation(summary = "Modifier un étudiant existant")
    @ApiResponse(responseCode = "200", description = "Étudiant modifié")
    @ApiResponse(responseCode = "404", description = "Aucun étudiant avec cet id")
    @PutMapping("/update/{id}")
    public ResponseEntity<Student> update(@PathVariable("id") int id, @Valid @RequestBody Student student) {
        return ResponseEntity.ok(studentService.update(id, student));
    }

    @Operation(summary = "Supprimer un étudiant")
    @ApiResponse(responseCode = "204", description = "Étudiant supprimé")
    @ApiResponse(responseCode = "404", description = "Aucun étudiant avec cet id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@Parameter(description = "Identifiant de l'étudiant") @PathVariable("id") int id) {
        boolean deleted = studentService.delete(id);
        return new ResponseEntity<>(deleted ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Lister tous les étudiants")
    @GetMapping("/all")
    public ResponseEntity<List<Student>> findAll() {
        return new ResponseEntity<>(studentService.findAll(), HttpStatus.OK);
    }

    // {id:\d+} : seuls les chiffres correspondent, "/students/all" ne peut donc jamais être pris pour un id
    @Operation(summary = "Consulter un étudiant")
    @ApiResponse(responseCode = "404", description = "Aucun étudiant avec cet id")
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Student> findById(@PathVariable("id") int id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @Operation(summary = "Nombre total d'étudiants")
    @GetMapping("/count")
    public ResponseEntity<Long> countStudent() {
        return new ResponseEntity<>(studentService.countStudents(), HttpStatus.OK);
    }

    @Operation(summary = "Nombre d'étudiants par année de naissance")
    @GetMapping("/byYear")
    public ResponseEntity<List<StatistiqueAnnee>> findByYear() {
        return new ResponseEntity<>(studentService.findNbrStudentByYear(), HttpStatus.OK);
    }

    @Operation(summary = "Étudiants d'une filière", description = "Triés par nom de famille.")
    @GetMapping("/filiere/{codeFiliere}")
    public ResponseEntity<List<Student>> findByFiliere(@PathVariable("codeFiliere") String codeFiliere) {
        return ResponseEntity.ok(studentService.findByFiliere(codeFiliere));
    }

    @Operation(summary = "Rechercher par nom de famille", description = "Recherche partielle, sans tenir compte des majuscules.")
    @GetMapping("/search")
    public ResponseEntity<List<Student>> rechercher(@Parameter(description = "Morceau du nom, par exemple \"ben\"")
                                                    @RequestParam("nom") String nom) {
        return ResponseEntity.ok(studentService.rechercherParNom(nom));
    }
}
