package com.example.demo.webatrio.repository;

import com.example.demo.webatrio.modele.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne, Long> {
} 