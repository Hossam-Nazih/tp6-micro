package com.example.studentmanagement.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Traduit les exceptions en réponses HTTP claires, pour tous les contrôleurs à la fois.
 * Sans cette classe, chacun de ces cas renverrait une erreur 500 générique.
 * ProblemDetail est le format d'erreur standard (RFC 7807) : {"status": 404, "detail": "..."}.
 */
@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ProblemDetail introuvable(RessourceIntrouvableException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    /** Échec de @Valid : la réponse liste chaque champ refusé avec son message, par exemple "cne" -> "...". */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail invalide(MethodArgumentNotValidException e) {
        Map<String, String> champs = new LinkedHashMap<>();
        for (FieldError erreur : e.getBindingResult().getFieldErrors()) {
            champs.putIfAbsent(erreur.getField(), erreur.getDefaultMessage());
        }
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Données invalides");
        probleme.setProperty("champs", champs);
        return probleme;
    }

    // Contrainte unique violée en base : CNE, email ou code déjà utilisé
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail conflit(DataIntegrityViolationException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "Une valeur unique (CNE, email ou code) est déjà utilisée");
    }
}
