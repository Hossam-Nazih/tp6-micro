package com.example.studentmanagement.service;

import com.example.studentmanagement.entity.Filiere;
import com.example.studentmanagement.entity.ModuleEnseignement;
import com.example.studentmanagement.exception.RessourceIntrouvableException;
import com.example.studentmanagement.repository.FiliereRepository;
import com.example.studentmanagement.repository.ModuleEnseignementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FiliereService {

    private final FiliereRepository filiereRepository;
    private final ModuleEnseignementRepository moduleRepository;

    public FiliereService(FiliereRepository filiereRepository, ModuleEnseignementRepository moduleRepository) {
        this.filiereRepository = filiereRepository;
        this.moduleRepository = moduleRepository;
    }

    @Transactional
    public Filiere create(Filiere filiere) {
        return filiereRepository.save(filiere);
    }

    public List<Filiere> findAll() {
        return filiereRepository.findAll();
    }

    public Filiere findByCode(String codeFiliere) {
        return filiereRepository.findByCodeFiliere(codeFiliere)
                .orElseThrow(() -> new RessourceIntrouvableException("Filière introuvable : " + codeFiliere));
    }

    public List<ModuleEnseignement> findModules(String codeFiliere) {
        findByCode(codeFiliere);   // 404 pour une filière inconnue, plutôt qu'une liste vide trompeuse
        return moduleRepository.findByFiliereCodeFiliereOrderBySemestreAscCodeModuleAsc(codeFiliere);
    }

    @Transactional
    public ModuleEnseignement ajouterModule(String codeFiliere, ModuleEnseignement module) {
        Filiere filiere = findByCode(codeFiliere);
        filiere.ajouterModule(module);
        return moduleRepository.save(module);
    }
}
