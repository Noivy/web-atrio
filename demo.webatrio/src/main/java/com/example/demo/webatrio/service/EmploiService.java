package com.example.demo.webatrio.service;

import com.example.demo.webatrio.modele.Emploi;
import com.example.demo.webatrio.modele.Personne;
import com.example.demo.webatrio.repository.EmploiRepository;
import com.example.demo.webatrio.repository.PersonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmploiService {

    private final EmploiRepository emploiRepository;
    private final PersonneRepository personneRepository;

    @Autowired
    public EmploiService(EmploiRepository emploiRepository, PersonneRepository personneRepository) {
        this.emploiRepository = emploiRepository;
        this.personneRepository = personneRepository;
    }

    public List<Emploi> obtenirTousEmplois() {
        return emploiRepository.findAll();
    }

    public List<Emploi> obtenirEmploisParPersonne(Long personneId) {
        Personne personne = personneRepository.findById(personneId)
                .orElseThrow(() -> new IllegalArgumentException("Personne non trouvée avec l'ID: " + personneId));
        return personne.getEmplois();
    }

    public Emploi obtenirEmploiParId(Long id) {
        return emploiRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Emploi non trouvé avec l'ID: " + id));
    }

    public Emploi ajouterEmploi(Emploi emploi, Long personneId) {
        Personne personne = personneRepository.findById(personneId)
                .orElseThrow(() -> new IllegalArgumentException("Personne non trouvée avec l'ID: " + personneId));
        
        emploi.setPersonne(personne);
        
        if (emploi.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }
        
        return emploiRepository.save(emploi);
    }

    public void supprimerEmploi(Long id) {
        emploiRepository.deleteById(id);
    }
} 