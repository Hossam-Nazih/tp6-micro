package com.example.studentmanagement.controller;

import com.example.studentmanagement.entity.Filiere;
import com.example.studentmanagement.entity.ModuleEnseignement;
import com.example.studentmanagement.service.FiliereService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Filières et leurs modules. Une filière est désignée par son code dans l'URL (/filieres/GI),
 * plus parlant qu'un identifiant numérique.
 */
@RestController
@RequestMapping("/filieres")
@Tag(name = "Filières", description = "Filières de formation et modules enseignés")
public class FiliereController {

    private final FiliereService filiereService;

    public FiliereController(FiliereService filiereService) {
        this.filiereService = filiereService;
    }

    @Operation(summary = "Lister les filières")
    @GetMapping
    public List<Filiere> findAll() {
        return filiereService.findAll();
    }

    @Operation(summary = "Créer une filière")
    @ApiResponse(responseCode = "201", description = "Filière créée")
    @ApiResponse(responseCode = "409", description = "Code déjà utilisé")
    @PostMapping
    public ResponseEntity<Filiere> create(@Valid @RequestBody Filiere filiere) {
        return new ResponseEntity<>(filiereService.create(filiere), HttpStatus.CREATED);
    }

    @Operation(summary = "Consulter une filière par son code")
    @ApiResponse(responseCode = "404", description = "Code inconnu")
    @GetMapping("/{code}")
    public Filiere findByCode(@PathVariable("code") String code) {
        return filiereService.findByCode(code);
    }

    @Operation(summary = "Modules d'une filière", description = "Triés par semestre puis par code.")
    @ApiResponse(responseCode = "404", description = "Code de filière inconnu")
    @GetMapping("/{code}/modules")
    public List<ModuleEnseignement> findModules(@PathVariable("code") String code) {
        return filiereService.findModules(code);
    }

    @Operation(summary = "Ajouter un module à une filière")
    @ApiResponse(responseCode = "201", description = "Module créé")
    @ApiResponse(responseCode = "404", description = "Code de filière inconnu")
    @PostMapping("/{code}/modules")
    public ResponseEntity<ModuleEnseignement> ajouterModule(@PathVariable("code") String code,
                                                            @Valid @RequestBody ModuleEnseignement module) {
        return new ResponseEntity<>(filiereService.ajouterModule(code, module), HttpStatus.CREATED);
    }
}
