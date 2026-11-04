package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StatistiqueAnnee;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.RessourceIntrouvableException;
import com.example.studentmanagement.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests au niveau HTTP : vraies requêtes envoyées à travers Spring MVC (sans serveur ni base), ce qui vérifie
 * ce que les tests unitaires ne voient pas : les URL, la conversion JSON, @Valid et les erreurs de GestionnaireErreurs.
 * @WebMvcTest ne charge que la couche web ; le service est remplacé par un mock (@MockitoBean).
 */
@WebMvcTest(StudentController.class)
class StudentControllerHttpTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private StudentService studentService;

    @Test
    void postEtudiantValideRenvoie201AvecLeJsonCree() throws Exception {
        Student enregistre = new Student("D130045678", "Bennani", "Salma", LocalDate.of(2003, 4, 12));
        enregistre.setId(7);
        when(studentService.save(any(Student.class))).thenReturn(enregistre);

        mvc.perform(post("/students/save").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cne": "D130045678", "nomFamille": "Bennani", "prenom": "Salma", "dateDeNaissance": "2003-04-12"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.dateDeNaissance").value("2003-04-12"));
    }

    @Test
    void postEtudiantInvalideRenvoie400AvecLeDetailDesChamps() throws Exception {
        mvc.perform(post("/students/save").contentType(MediaType.APPLICATION_JSON).content("""
                        {"cne": "123", "nomFamille": "", "prenom": "Salma", "dateDeNaissance": "2999-01-01"}
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.champs.cne").exists())
                .andExpect(jsonPath("$.champs.nomFamille").value("le nom de famille est obligatoire"))
                .andExpect(jsonPath("$.champs.dateDeNaissance").value("la date de naissance doit être dans le passé"));

        verify(studentService, never()).save(any());
    }

    @Test
    void getEtudiantInconnuRenvoie404() throws Exception {
        when(studentService.findById(99)).thenThrow(new RessourceIntrouvableException("Étudiant introuvable : 99"));

        mvc.perform(get("/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Étudiant introuvable : 99"));
    }

    @Test
    void deleteEtudiantInconnuRenvoie404() throws Exception {
        when(studentService.delete(99)).thenReturn(false);

        mvc.perform(delete("/students/delete/99")).andExpect(status().isNotFound());
    }

    @Test
    void byYearRenvoieUnObjetParAnnee() throws Exception {
        when(studentService.findNbrStudentByYear()).thenReturn(Arrays.asList(new StatistiqueAnnee(1985, 2)));

        mvc.perform(get("/students/byYear"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].annee").value(1985))
                .andExpect(jsonPath("$[0].nombreEtudiants").value(2));
    }

    @Test
    void countRenvoieLeNombreSeul() throws Exception {
        when(studentService.countStudents()).thenReturn(3L);

        mvc.perform(get("/students/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(3));
    }
}
