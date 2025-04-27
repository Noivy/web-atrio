import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { EmploiService } from '../../services/emploi.service';
import { Emploi } from '../../models/emploi.model';

@Component({
  selector: 'app-liste-emplois',
  templateUrl: './liste-emplois.component.html',
  styleUrls: ['./liste-emplois.component.css']
})
export class ListeEmploisComponent implements OnChanges {
  @Input() personneId: number | null = null;
  emplois: Emploi[] = [];
  chargementEnCours = false;

  constructor(private emploiService: EmploiService) { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['personneId'] && this.personneId) {
      this.chargerEmplois();
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
} 