package com.example.demo.webatrio.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmploiDTO {
    private Long id;
    private Long personneId;
    private String nomEntreprise;
    private String titrePoste;
    private LocalDate dateDebut;
    private LocalDate dateFin;
} 