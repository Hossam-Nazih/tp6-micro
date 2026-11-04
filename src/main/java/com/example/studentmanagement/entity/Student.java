package com.example.studentmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Étudiant inscrit dans l'établissement. Chaque instance correspond à une ligne de la table etudiants.
 *
 * Les contraintes de validation (@NotBlank, @Past...) sont vérifiées par @Valid dans le contrôleur,
 * avant tout accès à la base : une requête invalide reçoit une erreur 400 qui explique chaque champ fautif.
 */
@Entity
@Table(name = "etudiants")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Code national de l'étudiant : une lettre suivie de 9 chiffres, par exemple "D130045678"
    @NotBlank(message = "le CNE est obligatoire")
    @Pattern(regexp = "[A-Z]\\d{9}", message = "le CNE doit être une lettre majuscule suivie de 9 chiffres")
    @Column(nullable = false, unique = true, length = 10)
    private String cne;

    @NotBlank(message = "le nom de famille est obligatoire")
    @Size(max = 60)
    @Column(name = "nom_famille", nullable = false, length = 60)
    private String nomFamille;

    @NotBlank(message = "le prénom est obligatoire")
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String prenom;

    // LocalDate plutôt que java.util.Date + @Temporal(DATE) de l'énoncé : LocalDate ne contient que la date,
    // sans heure ni fuseau horaire, et JPA la range directement dans une colonne DATE.
    // En JSON, elle s'écrit et se lit au format "1985-09-01".
    @NotNull(message = "la date de naissance est obligatoire")
    @Past(message = "la date de naissance doit être dans le passé")
    @Column(name = "date_de_naissance", nullable = false)
    private LocalDate dateDeNaissance;

    @Email(message = "adresse email invalide")
    @Column(name = "email_institutionnel", unique = true, length = 120)
    private String emailInstitutionnel;

    // EAGER (valeur par défaut d'un @ManyToOne) : la filière fait partie du JSON de l'étudiant,
    // elle doit donc être chargée avant la fin de la transaction.
    @ManyToOne
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;

    public Student() {}

    public Student(String cne, String nomFamille, String prenom, LocalDate dateDeNaissance) {
        this.cne = cne;
        this.nomFamille = nomFamille;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCne() { return cne; }
    public void setCne(String cne) { this.cne = cne; }
    public String getNomFamille() { return nomFamille; }
    public void setNomFamille(String nomFamille) { this.nomFamille = nomFamille; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateDeNaissance() { return dateDeNaissance; }
    public void setDateDeNaissance(LocalDate dateDeNaissance) { this.dateDeNaissance = dateDeNaissance; }
    public String getEmailInstitutionnel() { return emailInstitutionnel; }
    public void setEmailInstitutionnel(String emailInstitutionnel) { this.emailInstitutionnel = emailInstitutionnel; }
    public Filiere getFiliere() { return filiere; }
    public void setFiliere(Filiere filiere) { this.filiere = filiere; }
}
