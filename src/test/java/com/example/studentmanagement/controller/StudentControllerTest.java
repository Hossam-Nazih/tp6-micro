package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StatistiqueAnnee;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitaires du contrôleur (étape 8) : le service est un mock Mockito, aucune base n'est utilisée.
 *
 * MockitoExtension plutôt que @SpringBootTest (énoncé) : @SpringBootTest démarre toute l'application,
 * connexion MySQL comprise, alors que ces tests n'en ont pas besoin. Ici ils tournent en quelques millisecondes,
 * même MySQL arrêté.
 */
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    // Mockito crée le contrôleur en passant le mock à son constructeur
    @InjectMocks
    private StudentController studentController;

    @Test
    void testSaveStudent() {
        Student student = new Student("D130045678", "Bennani", "Salma", LocalDate.of(2003, 4, 12));
        student.setId(1);
        when(studentService.save(any(Student.class))).thenReturn(student);

        ResponseEntity<Student> response = studentController.save(student);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Bennani", response.getBody().getNomFamille());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testDeleteStudent() {
        when(studentService.delete(1)).thenReturn(true);

        ResponseEntity<Void> response = studentController.delete(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(studentService).delete(1);
    }

    @Test
    void testDeleteStudentInexistant() {
        when(studentService.delete(99)).thenReturn(false);

        assertEquals(HttpStatus.NOT_FOUND, studentController.delete(99).getStatusCode());
    }

    @Test
    void testFindAllStudents() {
        when(studentService.findAll()).thenReturn(Arrays.asList(new Student(), new Student()));

        ResponseEntity<List<Student>> response = studentController.findAll();

        assertEquals(2, response.getBody().size());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCountStudents() {
        when(studentService.countStudents()).thenReturn(10L);

        ResponseEntity<Long> response = studentController.countStudent();

        assertEquals(10L, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindByYear() {
        when(studentService.findNbrStudentByYear()).thenReturn(Arrays.asList(
                new StatistiqueAnnee(2002, 2), new StatistiqueAnnee(2003, 1)));

        ResponseEntity<List<StatistiqueAnnee>> response = studentController.findByYear();

        assertEquals(2, response.getBody().size());
        assertEquals(2002, response.getBody().get(0).annee());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindByYearSansEtudiant() {
        when(studentService.findNbrStudentByYear()).thenReturn(Collections.emptyList());

        assertEquals(0, studentController.findByYear().getBody().size());
    }
}
