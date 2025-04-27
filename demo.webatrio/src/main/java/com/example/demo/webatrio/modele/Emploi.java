package com.example.demo.webatrio.modele;

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

@Entity
@Table(name = "emplois")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emploi {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nomEntreprise;
    
    private String titrePoste;
    
    @ManyToOne
    @JoinColumn(name = "personne_id")
    private Personne personne;
} 