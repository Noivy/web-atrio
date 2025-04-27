package com.example.demo.webatrio.controller;

import com.example.demo.webatrio.dto.EmploiDTO;
import com.example.demo.webatrio.dto.PersonneDTO;
import com.example.demo.webatrio.modele.Emploi;
import com.example.demo.webatrio.modele.Personne;
import com.example.demo.webatrio.service.EmploiService;
import com.example.demo.webatrio.service.PersonneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personnes")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Personne", description = "API de gestion des personnes")
public class PersonneController {

    private final PersonneService personneService;
    private final EmploiService emploiService;

    @Autowired
    public PersonneController(PersonneService personneService, EmploiService emploiService) {
        this.personneService = personneService;
        this.emploiService = emploiService;
    }

    @GetMapping
    @Operation(summary = "Obtenir toutes les personnes", description = "Récupère la liste de toutes les personnes enregistrées")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des personnes récupérée avec succès", 
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PersonneDTO.class)) }),
    })
    public ResponseEntity<List<PersonneDTO>> obtenirToutesPersonnes() {
        List<Personne> personnes = personneService.obtenirToutesPersonnes();
        List<PersonneDTO> personneDTOs = personnes.stream()
                .map(this::convertirVersDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(personneDTOs);
    }

    @GetMapping("/entreprise/{nomEntreprise}")
    @Operation(summary = "Obtenir les personnes par entreprise", 
            description = "Récupère les personnes qui travaillent ou ont travaillé pour l'entreprise spécifiée")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des personnes récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "Nom d'entreprise invalide ou vide")
    })
    public ResponseEntity<List<PersonneDTO>> obtenirPersonnesParEntreprise(
            @Parameter(description = "Nom de l'entreprise à rechercher") 
            @PathVariable String nomEntreprise) {
        try {
            List<Personne> personnes = personneService.obtenirPersonnesParEntreprise(nomEntreprise);
            List<PersonneDTO> personneDTOs = personnes.stream()
                    .map(this::convertirVersDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(personneDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/avec-emplois")
    @Operation(summary = "Obtenir les personnes avec emploi", 
            description = "Récupère toutes les personnes qui ont au moins un emploi")
    public ResponseEntity<List<PersonneDTO>> obtenirPersonnesAvecEmplois() {
        List<Personne> personnes = personneService.obtenirPersonnesAvecEmplois();
        List<PersonneDTO> personneDTOs = personnes.stream()
                .map(this::convertirVersDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(personneDTOs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une personne par ID", 
            description = "Récupère les détails d'une personne spécifique par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personne trouvée"),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée")
    })
    public ResponseEntity<PersonneDTO> obtenirPersonneParId(
            @Parameter(description = "ID de la personne à rechercher") 
            @PathVariable Long id) {
        Personne personne = personneService.obtenirPersonneParId(id);
        if (personne == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirVersDTO(personne));
    }

    @PostMapping
    @Operation(summary = "Créer une personne", 
            description = "Crée une nouvelle personne avec les données fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Personne créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données de personne invalides")
    })
    public ResponseEntity<?> creerPersonne(@RequestBody PersonneDTO personneDTO) {
        try {
            Personne personne = convertirVersEntite(personneDTO);
            Personne nouvellePersonne = personneService.enregistrerPersonne(personne);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertirVersDTO(nouvellePersonne));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une personne", 
            description = "Supprime une personne existante par son ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Personne supprimée avec succès"),
            @ApiResponse(responseCode = "404", description = "Personne non trouvée")
    })
    public ResponseEntity<Void> supprimerPersonne(
            @Parameter(description = "ID de la personne à supprimer") 
            @PathVariable Long id) {
        personneService.supprimerPersonne(id);
        return ResponseEntity.noContent().build();
    }

    private PersonneDTO convertirVersDTO(Personne personne) {
        // Calculer l'âge explicitement
        int age;
        if (personne.getDateNaissance() != null) {
            LocalDate dateNaissance = personne.getDateNaissance();
            LocalDate maintenant = LocalDate.now();
            age = Period.between(dateNaissance, maintenant).getYears();
        } else {
            age = 0;
        }
        
        // Récupérer les emplois actuels (sans date de fin)
        List<EmploiDTO> emploisActuels = new ArrayList<>();
        if (personne.getEmplois() != null) {
            emploisActuels = personne.getEmplois().stream()
                    .filter(Emploi::estEmploiActuel)
                    .map(this::convertirEmploiVersDTO)
                    .collect(Collectors.toList());
        }
        
        PersonneDTO dto = new PersonneDTO(
                personne.getId(),
                personne.getPrenom(),
                personne.getNom(),
                personne.getDateNaissance(),
                age,
                emploisActuels
        );
        
        return dto;
    }

    private Personne convertirVersEntite(PersonneDTO dto) {
        Personne personne = new Personne();
        personne.setId(dto.getId());
        personne.setPrenom(dto.getPrenom());
        personne.setNom(dto.getNom());
        personne.setDateNaissance(dto.getDateNaissance());
        return personne;
    }
    
    private EmploiDTO convertirEmploiVersDTO(Emploi emploi) {
        return new EmploiDTO(
                emploi.getId(),
                emploi.getPersonne().getId(),
                emploi.getNomEntreprise(),
                emploi.getTitrePoste(),
                emploi.getDateDebut(),
                emploi.getDateFin()
        );
    }
} 