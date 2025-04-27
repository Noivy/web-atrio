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
  
  nomEntreprise: string = '';
  nomEntrepriseRecherche: string = '';
  rechercheModeActif: boolean = false;

  constructor(private personneService: PersonneService) { }

  ngOnInit(): void {
    this.personneService.personnes$.subscribe(personnes => {
      if (!this.rechercheModeActif) {
        this.personnes = this.trierPersonnes(personnes);
        
        if (this.personneSelectionnee) {
          const personneActualisee = personnes.find(p => p.id === this.personneSelectionnee?.id);
          if (personneActualisee) {
            this.personneSelectionnee = personneActualisee;
          }
        }
      }
    });
    
    this.chargerPersonnes();
  }

  rechercherParEntreprise(): void {
    if (!this.nomEntreprise || this.nomEntreprise.trim() === '') {
      return;
    }
    
    this.rechercheModeActif = true;
    this.nomEntrepriseRecherche = this.nomEntreprise;
    this.personneSelectionnee = null;
    
    this.personneService.obtenirPersonnesParEntreprise(this.nomEntreprise).subscribe({
      next: (personnes) => {
        this.personnes = this.trierPersonnes(personnes);
      },
      error: () => {
        this.personnes = [];
      }
    });
  }
  
  resetRecherche(): void {
    this.rechercheModeActif = false;
    this.nomEntreprise = '';
    this.nomEntrepriseRecherche = '';
    this.personneSelectionnee = null;
    this.chargerPersonnes();
  }
  
  afficherPersonnesAvecEmplois(): void {
    this.rechercheModeActif = true;
    this.nomEntrepriseRecherche = "toutes les entreprises";
    this.personneSelectionnee = null;
    
    this.personneService.obtenirPersonnesAvecEmplois().subscribe({
      next: (personnes) => {
        this.personnes = this.trierPersonnes(personnes);
      },
      error: () => {
        this.personnes = [];
      }
    });
  }

  chargerPersonnes(): void {
    this.personneService.obtenirToutesPersonnes().subscribe({
      error: () => {}
    });
  }

  selectionnerPersonne(personne: Personne): void {
    this.personneService.obtenirPersonneParId(personne.id!).subscribe({
      next: (personneActualisee) => {
        this.personneSelectionnee = personneActualisee;
      },
      error: () => {}
    });
  }
  
  private trierPersonnes(personnes: Personne[]): Personne[] {
    return [...personnes].sort((a, b) => {
      const comparaisonNom = a.nom.localeCompare(b.nom);
      if (comparaisonNom !== 0) {
        return comparaisonNom;
      }
      return a.prenom.localeCompare(b.prenom);
    });
  }
} 