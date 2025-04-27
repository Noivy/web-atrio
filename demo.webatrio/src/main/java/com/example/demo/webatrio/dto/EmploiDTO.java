package com.example.demo.webatrio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objet de transfert pour les données d'un emploi")
public class EmploiDTO {
    @Schema(description = "Identifiant unique de l'emploi", example = "1")
    private Long id;
    
    @Schema(description = "Identifiant de la personne associée", example = "1")
    private Long personneId;
    
    @Schema(description = "Nom de l'entreprise", example = "Web-Atrio")
    private String nomEntreprise;
    
    @Schema(description = "Titre du poste occupé", example = "Développeur Java")
    private String titrePoste;
    
    @Schema(description = "Date de début de l'emploi", example = "2020-01-01", format = "date")
    private LocalDate dateDebut;
    
    @Schema(description = "Date de fin de l'emploi (null si emploi actuel)", example = "2022-12-31", format = "date")
    private LocalDate dateFin;
} 