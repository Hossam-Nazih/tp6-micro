package com.example.studentmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Formation suivie par les étudiants (Génie informatique, Réseaux...). Entité ajoutée au modèle de l'énoncé.
 */
@Entity
@Table(name = "filieres")
public class Filiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Code court utilisé dans les URL : /filieres/GI/modules
    @NotBlank(message = "le code de la filière est obligatoire")
    @Size(max = 10)
    @Column(name = "code_filiere", nullable = false, unique = true, length = 10)
    private String codeFiliere;

    @NotBlank(message = "l'intitulé est obligatoire")
    @Column(nullable = false, length = 120)
    private String intitule;

    // @JsonIgnore sur les deux listes : sinon le JSON d'une filière contiendrait ses étudiants, dont chacun
    // contient sa filière, qui contient ses étudiants... (boucle infinie). Elles ont leurs propres endpoints.
    @JsonIgnore
    @OneToMany(mappedBy = "filiere")
    private List<Student> etudiants = new ArrayList<>();

    // Un module n'existe pas sans sa filière : il est enregistré et supprimé avec elle
    @JsonIgnore
    @OneToMany(mappedBy = "filiere", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ModuleEnseignement> modules = new ArrayList<>();

    public Filiere() {}

    public Filiere(String codeFiliere, String intitule) {
        this.codeFiliere = codeFiliere;
        this.intitule = intitule;
    }

    public void ajouterModule(ModuleEnseignement module) {
        modules.add(module);
        module.setFiliere(this);
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCodeFiliere() { return codeFiliere; }
    public void setCodeFiliere(String codeFiliere) { this.codeFiliere = codeFiliere; }
    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }
    public List<Student> getEtudiants() { return etudiants; }
    public List<ModuleEnseignement> getModules() { return modules; }
}
