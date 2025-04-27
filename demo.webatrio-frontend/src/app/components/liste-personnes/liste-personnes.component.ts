import { Component, OnInit } from '@angular/core';
import { PersonneService } from '../../services/personne.service';
import { Personne } from '../../models/personne.model';

@Component({
  selector: 'app-liste-personnes',
  templateUrl: './liste-personnes.component.html',
  styleUrls: ['./liste-personnes.component.css']
})
export class ListePersonnesComponent implements OnInit {
  personnes: Personne[] = [];
  personneSelectionnee: Personne | null = null;

  constructor(private personneService: PersonneService) { }

  ngOnInit(): void {
    this.chargerPersonnes();
  }

  chargerPersonnes(): void {
    this.personneService.obtenirToutesPersonnes().subscribe({
      next: (data) => {
        this.personnes = data;
      },
      error: (e) => {
        console.error('Erreur lors du chargement des personnes', e);
      }
    });
  }

  selectionnerPersonne(personne: Personne): void {
    this.personneSelectionnee = personne;
  }
} 