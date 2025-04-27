import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { EmploiService } from '../../services/emploi.service';
import { Emploi } from '../../models/emploi.model';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-liste-emplois',
  templateUrl: './liste-emplois.component.html',
  styleUrls: ['./liste-emplois.component.css']
})
export class ListeEmploisComponent implements OnChanges {
  @Input() personneId: number | null = null;
  emplois: Emploi[] = [];
  chargementEnCours = false;
  
  dateDebut: string = '';
  dateFin: string = '';
  modeFiltrage = false;

  constructor(private emploiService: EmploiService) {
    const aujourdhui = new Date();
    const ilYaUnAn = new Date();
    ilYaUnAn.setFullYear(aujourdhui.getFullYear() - 1);
    
    this.dateDebut = this.formaterDate(ilYaUnAn);
    this.dateFin = this.formaterDate(aujourdhui);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['personneId'] && this.personneId) {
      this.reinitialiserRecherche();
    }
  }

  chargerEmplois(): void {
    if (!this.personneId) return;
    
    this.chargementEnCours = true;
    this.emploiService.obtenirEmploisParPersonne(this.personneId).subscribe({
      next: (data) => {
        this.emplois = data;
        this.chargementEnCours = false;
      },
      error: () => {
        this.chargementEnCours = false;
      }
    });
  }
  
  rechercherEmploisParPeriode(): void {
    if (!this.personneId || !this.dateDebut || !this.dateFin) return;
    
    this.modeFiltrage = true;
    this.chargementEnCours = true;
    
    this.emploiService.obtenirEmploisParPersonneEtPeriode(
      this.personneId, 
      this.dateDebut, 
      this.dateFin
    ).subscribe({
      next: (data) => {
        this.emplois = data;
        this.chargementEnCours = false;
      },
      error: () => {
        this.emplois = [];
        this.chargementEnCours = false;
      }
    });
  }
  
  reinitialiserRecherche(): void {
    this.modeFiltrage = false;
    this.chargerEmplois();
  }

  possedeEmploiActuel(emploi: Emploi): boolean {
    return !emploi.dateFin;
  }

  supprimerEmploi(id: number): void {
    this.emploiService.supprimerEmploi(id).subscribe({
      next: () => {
        this.emplois = this.emplois.filter(e => e.id !== id);
      }
    });
  }
  
  private formaterDate(date: Date): string {
    return formatDate(date, 'yyyy-MM-dd', 'en-US');
  }
} 