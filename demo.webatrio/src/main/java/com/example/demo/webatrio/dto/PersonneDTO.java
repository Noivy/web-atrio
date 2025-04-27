package com.example.demo.webatrio.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonneDTO {
    private Long id;
    private String prenom;
    private String nom;
    private LocalDate dateNaissance;
    private int age;
    private List<EmploiDTO> emploisActuels;
} 