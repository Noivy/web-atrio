package com.example.demo.webatrio.controller;

import com.example.demo.webatrio.dto.EmploiDTO;
import com.example.demo.webatrio.modele.Emploi;
import com.example.demo.webatrio.service.EmploiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emplois")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Emploi", description = "API de gestion des emplois")
public class EmploiController {

    private final EmploiService emploiService;

    @Autowired
    public EmploiController(EmploiService emploiService) {
        this.emploiService = emploiService;
    }

    @GetMapping
    @Operation(summary = "Obtenir tous les emplois", description = "Récupère la liste de tous les emplois enregistrés")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des emplois récupérée avec succès",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EmploiDTO.class)) })
    })
    public ResponseEntity<List<EmploiDTO>> obtenirTousEmplois() {
        List<Emploi> emplois = emploiService.obtenirTousEmplois();
        List<EmploiDTO> emploiDTOs = emplois.stream()
                .map(this::convertirVersDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emploiDTOs);
    }

    @GetMapping("/personne/{personneId}")
    @Operation(summary = "Obtenir les emplois d'une personne", 
            description = "Récupère tous les emplois associés à une personne spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emplois récupérés avec succès"),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée")
    })
    public ResponseEntity<List<EmploiDTO>> obtenirEmploisParPersonne(
            @Parameter(description = "ID de la personne") 
            @PathVariable Long personneId) {
        try {
            List<Emploi> emplois = emploiService.obtenirEmploisParPersonne(personneId);
            List<EmploiDTO> emploiDTOs = emplois.stream()
                    .map(this::convertirVersDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(emploiDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/personne/{personneId}/periode")
    @Operation(summary = "Obtenir les emplois d'une personne par période", 
            description = "Récupère les emplois d'une personne qui se chevauchent avec la période spécifiée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emplois récupérés avec succès"),
            @ApiResponse(responseCode = "400", description = "Paramètres invalides"),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée")
    })
    public ResponseEntity<List<EmploiDTO>> obtenirEmploisParPersonneEtPeriode(
            @Parameter(description = "ID de la personne") 
            @PathVariable Long personneId,
            @Parameter(description = "Date de début de la période (format ISO: YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin de la période (format ISO: YYYY-MM-DD)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        
        try {
            List<Emploi> emplois = emploiService.obtenirEmploisParPersonneEtPeriode(personneId, dateDebut, dateFin);
            List<EmploiDTO> emploiDTOs = emplois.stream()
                    .map(this::convertirVersDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(emploiDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un emploi par ID", description = "Récupère un emploi spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Emploi trouvé"),
            @ApiResponse(responseCode = "404", description = "Emploi non trouvé")
    })
    public ResponseEntity<EmploiDTO> obtenirEmploiParId(
            @Parameter(description = "ID de l'emploi") 
            @PathVariable Long id) {
        try {
            Emploi emploi = emploiService.obtenirEmploiParId(id);
            return ResponseEntity.ok(convertirVersDTO(emploi));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/personne/{personneId}")
    @Operation(summary = "Ajouter un emploi à une personne", 
            description = "Crée un nouvel emploi pour une personne existante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Emploi créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données d'emploi invalides"),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée")
    })
    public ResponseEntity<?> ajouterEmploi(
            @RequestBody EmploiDTO emploiDTO, 
            @Parameter(description = "ID de la personne") 
            @PathVariable Long personneId) {
        try {
            Emploi emploi = convertirVersEntite(emploiDTO);
            Emploi nouvelEmploi = emploiService.ajouterEmploi(emploi, personneId);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertirVersDTO(nouvelEmploi));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un emploi", description = "Supprime un emploi existant par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Emploi supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Emploi non trouvé")
    })
    public ResponseEntity<Void> supprimerEmploi(
            @Parameter(description = "ID de l'emploi") 
            @PathVariable Long id) {
        try {
            emploiService.supprimerEmploi(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private EmploiDTO convertirVersDTO(Emploi emploi) {
        return new EmploiDTO(
                emploi.getId(),
                emploi.getPersonne().getId(),
                emploi.getNomEntreprise(),
                emploi.getTitrePoste(),
                emploi.getDateDebut(),
                emploi.getDateFin()
        );
    }

    private Emploi convertirVersEntite(EmploiDTO dto) {
        Emploi emploi = new Emploi();
        emploi.setId(dto.getId());
        emploi.setNomEntreprise(dto.getNomEntreprise());
        emploi.setTitrePoste(dto.getTitrePoste());
        emploi.setDateDebut(dto.getDateDebut());
        emploi.setDateFin(dto.getDateFin());
        return emploi;
    }
} 