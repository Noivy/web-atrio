package com.example.demo.webatrio.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Objet de transfert pour les données d'une personne")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonneDTO {
    @Schema(description = "Identifiant unique de la personne", example = "1")
    private Long id;
    
    @Schema(description = "Prénom de la personne", example = "Jean")
    private String prenom;
    
    @Schema(description = "Nom de la personne", example = "Dupont")
    private String nom;
    
    @Schema(description = "Date de naissance de la personne", example = "1990-01-15", format = "date")
    private LocalDate dateNaissance;
    
    @Schema(description = "Âge calculé de la personne", example = "33")
    private Integer age;
    
    @Schema(description = "Liste des emplois actuels de la personne (sans date de fin)")
    private List<EmploiDTO> emploisActuels;
} 