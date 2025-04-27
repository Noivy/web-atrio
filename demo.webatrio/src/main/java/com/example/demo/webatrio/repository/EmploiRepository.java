package com.example.demo.webatrio.repository;

import com.example.demo.webatrio.modele.Emploi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploiRepository extends JpaRepository<Emploi, Long> {
} 