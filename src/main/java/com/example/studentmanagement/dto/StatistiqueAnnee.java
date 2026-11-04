package com.example.studentmanagement.dto;

/**
 * Une ligne de la statistique "étudiants par année de naissance".
 * Remplie directement par la requête JPQL (select new ...StatistiqueAnnee(...)) et renvoyée en JSON
 * sous la forme {"annee": 1985, "nombreEtudiants": 2}, plus lisible que le tableau [1985, 2] de l'énoncé.
 */
public record StatistiqueAnnee(int annee, long nombreEtudiants) {
}
