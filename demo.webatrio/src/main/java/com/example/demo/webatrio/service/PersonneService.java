package com.example.demo.webatrio.service;

import com.example.demo.webatrio.modele.Personne;
import com.example.demo.webatrio.repository.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonneService {

    private final PersonneRepository personneRepository;

    @Autowired
    public PersonneService(PersonneRepository personneRepository) {
        this.personneRepository = personneRepository;
    }

    public List<Personne> obtenirToutesPersonnes() {
        return personneRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Personne::getNom)
                        .thenComparing(Personne::getPrenom))
                .collect(Collectors.toList());
    }

    public Personne obtenirPersonneParId(Long id) {
        return personneRepository.findById(id).orElse(null);
    }
    
    public List<Personne> obtenirPersonnesParEntreprise(String nomEntreprise) {
        if (nomEntreprise == null || nomEntreprise.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'entreprise ne peut pas être vide");
        }
        return personneRepository.findByNomEntreprise(nomEntreprise)
                .stream()
                .sorted(Comparator.comparing(Personne::getNom)
                        .thenComparing(Personne::getPrenom))
                .collect(Collectors.toList());
    }
    
    public List<Personne> obtenirPersonnesAvecEmplois() {
        return personneRepository.findAllWithJobs()
                .stream()
                .sorted(Comparator.comparing(Personne::getNom)
                        .thenComparing(Personne::getPrenom))
                .collect(Collectors.toList());
    }

    public Personne enregistrerPersonne(Personne personne) {
        validerAge(personne);
        return personneRepository.save(personne);
    }

    public void supprimerPersonne(Long id) {
        personneRepository.deleteById(id);
    }

    private void validerAge(Personne personne) {
        LocalDate dateNaissance = personne.getDateNaissance();
        if (dateNaissance != null) {
            int age = Period.between(dateNaissance, LocalDate.now()).getYears();
            if (age >= 150) {
                throw new IllegalArgumentException("L'âge de la personne ne peut pas dépasser 150 ans");
            }
        }
    }
    
    public int calculerAge(LocalDate dateNaissance) {
        if (dateNaissance == null) {
            return 0;
        }
        return Period.between(dateNaissance, LocalDate.now()).getYears();
    }
} 