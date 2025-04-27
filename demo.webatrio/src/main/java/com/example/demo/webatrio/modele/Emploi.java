package com.example.demo.webatrio.modele;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "emplois")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entité représentant un emploi d'une personne")
public class Emploi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de l'emploi", example = "1")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "personne_id")
    @JsonIgnore
    @Schema(description = "Personne associée à cet emploi")
    private Personne personne;
    
    @Schema(description = "Nom de l'entreprise", example = "Web-Atrio")
    private String nomEntreprise;
    
    @Schema(description = "Titre du poste occupé", example = "Développeur Java")
    private String titrePoste;
    
    @Schema(description = "Date de début de l'emploi", example = "2020-01-01", format = "date")
    private LocalDate dateDebut;
    
    @Schema(description = "Date de fin de l'emploi (null si emploi actuel)", example = "2022-12-31", format = "date")
    private LocalDate dateFin;
    
    public boolean estEmploiActuel() {
        return this.dateFin == null;
    }
} 