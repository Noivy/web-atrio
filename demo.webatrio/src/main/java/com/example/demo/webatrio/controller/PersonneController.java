package com.example.demo.webatrio.controller;

import com.example.demo.webatrio.dto.EmploiDTO;
import com.example.demo.webatrio.dto.PersonneDTO;
import com.example.demo.webatrio.modele.Emploi;
import com.example.demo.webatrio.modele.Personne;
import com.example.demo.webatrio.service.EmploiService;
import com.example.demo.webatrio.service.PersonneService;
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
public class PersonneController {

    private final PersonneService personneService;
    private final EmploiService emploiService;

    @Autowired
    public PersonneController(PersonneService personneService, EmploiService emploiService) {
        this.personneService = personneService;
        this.emploiService = emploiService;
    }

    @GetMapping
    public ResponseEntity<List<PersonneDTO>> obtenirToutesPersonnes() {
        List<Personne> personnes = personneService.obtenirToutesPersonnes();
        List<PersonneDTO> personneDTOs = personnes.stream()
                .map(this::convertirVersDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(personneDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonneDTO> obtenirPersonneParId(@PathVariable Long id) {
        Personne personne = personneService.obtenirPersonneParId(id);
        if (personne == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertirVersDTO(personne));
    }

    @PostMapping
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
    public ResponseEntity<Void> supprimerPersonne(@PathVariable Long id) {
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
            System.out.println("Calcul de l'âge pour " + personne.getNom() + " " + personne.getPrenom() + ":");
            System.out.println(" - Date de naissance: " + dateNaissance);
            System.out.println(" - Date actuelle: " + maintenant);
            System.out.println(" - Âge calculé: " + age);
        } else {
            age = 0;
            System.out.println("Pas de calcul d'âge car la date de naissance est null pour " + personne.getNom() + " " + personne.getPrenom());
        }
        
        // Récupérer les emplois actuels (sans date de fin)
        List<EmploiDTO> emploisActuels = new ArrayList<>();
        if (personne.getEmplois() != null) {
            emploisActuels = personne.getEmplois().stream()
                    .filter(Emploi::estEmploiActuel)
                    .map(this::convertirEmploiVersDTO)
                    .collect(Collectors.toList());
        }
        
        // Log pour le débogage
        System.out.println("Personne: " + personne.getNom() + " " + personne.getPrenom());
        System.out.println("Age: " + age);
        System.out.println("Nombre d'emplois actuels: " + emploisActuels.size());
        emploisActuels.forEach(e -> System.out.println(" - " + e.getTitrePoste() + " chez " + e.getNomEntreprise()));
        
        PersonneDTO dto = new PersonneDTO(
                personne.getId(),
                personne.getPrenom(),
                personne.getNom(),
                personne.getDateNaissance(),
                age,
                emploisActuels
        );
        
        System.out.println("DTO créé avec âge: " + dto.getAge());
        
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