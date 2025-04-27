package com.example.demo.webatrio.controller;

import com.example.demo.webatrio.dto.PersonneDTO;
import com.example.demo.webatrio.modele.Personne;
import com.example.demo.webatrio.service.PersonneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/personnes")
@CrossOrigin(origins = "http://localhost:4200")
public class PersonneController {

    private final PersonneService personneService;

    @Autowired
    public PersonneController(PersonneService personneService) {
        this.personneService = personneService;
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
        return new PersonneDTO(
                personne.getId(),
                personne.getPrenom(),
                personne.getNom(),
                personne.getDateNaissance()
        );
    }

    private Personne convertirVersEntite(PersonneDTO dto) {
        Personne personne = new Personne();
        personne.setId(dto.getId());
        personne.setPrenom(dto.getPrenom());
        personne.setNom(dto.getNom());
        personne.setDateNaissance(dto.getDateNaissance());
        return personne;
    }
} 