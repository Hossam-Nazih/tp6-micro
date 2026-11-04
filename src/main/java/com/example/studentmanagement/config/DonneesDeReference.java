package com.example.studentmanagement.config;

import com.example.studentmanagement.entity.Filiere;
import com.example.studentmanagement.entity.ModuleEnseignement;
import com.example.studentmanagement.repository.FiliereRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Au premier démarrage, crée deux filières et leurs modules pour que l'API soit utilisable tout de suite :
 * un étudiant peut être inscrit avec "filiere": {"id": 1} sans créer de filière d'abord.
 * Ne fait rien si des filières existent déjà, les données ne sont donc jamais dupliquées.
 */
@Component
public class DonneesDeReference implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DonneesDeReference.class);

    private final FiliereRepository filiereRepository;

    public DonneesDeReference(FiliereRepository filiereRepository) {
        this.filiereRepository = filiereRepository;
    }

    @Override
    public void run(String... args) {
        if (filiereRepository.count() > 0) {
            return;
        }
        Filiere gi = new Filiere("GI", "Génie informatique");
        gi.ajouterModule(new ModuleEnseignement("GI-S1-ALGO", "Algorithmique et programmation", 64, 1));
        gi.ajouterModule(new ModuleEnseignement("GI-S3-BDD", "Bases de données relationnelles", 48, 3));
        gi.ajouterModule(new ModuleEnseignement("GI-S5-JEE", "Développement Java EE et Spring", 56, 5));

        Filiere rt = new Filiere("RT", "Réseaux et télécommunications");
        rt.ajouterModule(new ModuleEnseignement("RT-S1-ELEC", "Électronique numérique", 48, 1));
        rt.ajouterModule(new ModuleEnseignement("RT-S4-ROUT", "Routage et commutation", 52, 4));

        filiereRepository.save(gi);
        filiereRepository.save(rt);
        LOG.info("Filières de référence créées : GI (id {}), RT (id {})", gi.getId(), rt.getId());
    }
}
