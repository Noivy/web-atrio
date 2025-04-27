package com.example.demo.webatrio.modele;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personnes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entité représentant une personne")
public class Personne {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identifiant unique de la personne", example = "1")
    private Long id;
    
    @Schema(description = "Prénom de la personne", example = "Jean")
    private String prenom;
    
    @Schema(description = "Nom de la personne", example = "Dupont")
    private String nom;
    
    @Schema(description = "Date de naissance de la personne", example = "1990-01-15", format = "date")
    private LocalDate dateNaissance;
    
    @OneToMany(mappedBy = "personne", cascade = jakarta.persistence.CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Liste des emplois de la personne")
    private List<Emploi> emplois = new ArrayList<>();
} 