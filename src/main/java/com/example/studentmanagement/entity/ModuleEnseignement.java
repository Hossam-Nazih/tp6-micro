package com.example.studentmanagement.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Matière enseignée dans une filière. Entité ajoutée au modèle de l'énoncé.
 * Nommée ModuleEnseignement et non Module : java.lang.Module existe déjà depuis Java 9.
 */
@Entity
@Table(name = "modules_enseignement")
public class ModuleEnseignement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "le code du module est obligatoire")
    @Column(name = "code_module", nullable = false, unique = true, length = 15)
    private String codeModule;

    @NotBlank(message = "l'intitulé est obligatoire")
    @Column(nullable = false, length = 120)
    private String intitule;

    @Positive(message = "le volume horaire doit être positif")
    @Column(name = "volume_horaire", nullable = false)
    private int volumeHoraire;

    @Min(1) @Max(6)
    @Column(nullable = false)
    private int semestre;

    // READ_ONLY : visible dans le JSON renvoyé, mais ignoré dans le JSON reçu.
    // La filière d'un nouveau module vient de l'URL (POST /filieres/{code}/modules), pas du corps de la requête.
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne(optional = false)
    @JoinColumn(name = "filiere_id", nullable = false)
    private Filiere filiere;

    public ModuleEnseignement() {}

    public ModuleEnseignement(String codeModule, String intitule, int volumeHoraire, int semestre) {
        this.codeModule = codeModule;
        this.intitule = intitule;
        this.volumeHoraire = volumeHoraire;
        this.semestre = semestre;
    }

    public Integer getId() { return id; }
    public String getCodeModule() { return codeModule; }
    public void setCodeModule(String codeModule) { this.codeModule = codeModule; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public int getVolumeHoraire() { return volumeHoraire; }
    public void setVolumeHoraire(int volumeHoraire) { this.volumeHoraire = volumeHoraire; }
    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }
    public Filiere getFiliere() { return filiere; }
    public void setFiliere(Filiere filiere) { this.filiere = filiere; }
}
