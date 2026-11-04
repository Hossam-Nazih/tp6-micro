package com.example.studentmanagement.exception;

/**
 * Levée par un service quand l'élément demandé n'existe pas ; GestionnaireErreurs la transforme en 404.
 */
public class RessourceIntrouvableException extends RuntimeException {

    public RessourceIntrouvableException(String message) {
        super(message);
    }
}
