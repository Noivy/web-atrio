package com.example.demo.webatrio.repository;

import com.example.demo.webatrio.modele.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
    
    @Query("SELECT DISTINCT p FROM Personne p JOIN p.emplois e WHERE LOWER(e.nomEntreprise) LIKE LOWER(CONCAT('%', :nomEntreprise, '%'))")
    List<Personne> findByNomEntreprise(@Param("nomEntreprise") String nomEntreprise);
    
    @Query("SELECT DISTINCT p FROM Personne p JOIN p.emplois e")
    List<Personne> findAllWithJobs();
} 