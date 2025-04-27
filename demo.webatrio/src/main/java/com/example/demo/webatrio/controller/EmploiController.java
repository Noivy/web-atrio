package com.example.demo.webatrio.controller;

import com.example.demo.webatrio.dto.EmploiDTO;
import com.example.demo.webatrio.modele.Emploi;
import com.example.demo.webatrio.service.EmploiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emplois")
@CrossOrigin(origins = "http://localhost:4200")
public class EmploiController {

    private final EmploiService emploiService;

    @Autowired
    public EmploiController(EmploiService emploiService) {
        this.emploiService = emploiService;
    }

    @GetMapping
    public ResponseEntity<List<EmploiDTO>> obtenirTousEmplois() {
        List<Emploi> emplois = emploiService.obtenirTousEmplois();
        List<EmploiDTO> emploiDTOs = emplois.stream()
                .map(this::convertirVersDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(emploiDTOs);
    }

    @GetMapping("/personne/{personneId}")
    public ResponseEntity<List<EmploiDTO>> obtenirEmploisParPersonne(@PathVariable Long personneId) {
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

    @GetMapping("/{id}")
    public ResponseEntity<EmploiDTO> obtenirEmploiParId(@PathVariable Long id) {
        try {
            Emploi emploi = emploiService.obtenirEmploiParId(id);
            return ResponseEntity.ok(convertirVersDTO(emploi));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/personne/{personneId}")
    public ResponseEntity<?> ajouterEmploi(@RequestBody EmploiDTO emploiDTO, @PathVariable Long personneId) {
        try {
            Emploi emploi = convertirVersEntite(emploiDTO);
            Emploi nouvelEmploi = emploiService.ajouterEmploi(emploi, personneId);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertirVersDTO(nouvelEmploi));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerEmploi(@PathVariable Long id) {
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