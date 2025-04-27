package com.example.demo.webatrio.repository;

import com.example.demo.webatrio.modele.Emploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmploiRepository extends JpaRepository<Emploi, Long> {
    
    List<Emploi> findByPersonneId(Long personneId);
    
    @Query("SELECT e FROM Emploi e WHERE e.personne.id = :personneId AND " +
           "((e.dateDebut >= :dateDebut AND e.dateDebut <= :dateFin) OR " +
           "(e.dateFin >= :dateDebut AND e.dateFin <= :dateFin) OR " +
           "(e.dateDebut <= :dateDebut AND (e.dateFin >= :dateFin OR e.dateFin IS NULL)))")
    List<Emploi> findByPersonneIdAndDateRange(
            @Param("personneId") Long personneId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);
} 