import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap, map } from 'rxjs/operators';
import { Personne } from '../models/personne.model';

@Injectable({
  providedIn: 'root'
})
export class PersonneService {
  private apiUrl = 'http://localhost:8080/api/personnes';
  private personnesSubject = new BehaviorSubject<Personne[]>([]);
  public personnes$ = this.personnesSubject.asObservable();

  constructor(private http: HttpClient) {
    this.rafraichirPersonnes();
  }

  // Fonction pour calculer l'âge à partir de la date de naissance
  private calculerAge(dateNaissance: string): number {
    if (!dateNaissance) return 0;
    
    const dateNaissanceObj = new Date(dateNaissance);
    const aujourdhui = new Date();
    
    let age = aujourdhui.getFullYear() - dateNaissanceObj.getFullYear();
    const mois = aujourdhui.getMonth() - dateNaissanceObj.getMonth();
    
    if (mois < 0 || (mois === 0 && aujourdhui.getDate() < dateNaissanceObj.getDate())) {
      age--;
    }
    
    return age;
  }

  obtenirToutesPersonnes(): Observable<Personne[]> {
    return this.http.get<Personne[]>(this.apiUrl)
      .pipe(
        map(personnes => {
          return personnes.map(p => {
            const personneCorrigee: Personne = { ...p };
            
            if (p.age === undefined || p.age === null || isNaN(Number(p.age)) || Number(p.age) === 0) {
              personneCorrigee.age = this.calculerAge(p.dateNaissance);
            } else {
              personneCorrigee.age = Number(p.age);
            }
            
            if (!Array.isArray(p.emploisActuels)) {
              personneCorrigee.emploisActuels = [];
            }
            
            return personneCorrigee;
          });
        }),
        tap(personnes => {
          this.personnesSubject.next(personnes);
        })
      );
  }

  obtenirPersonnesParEntreprise(nomEntreprise: string): Observable<Personne[]> {
    const url = `${this.apiUrl}/entreprise/${encodeURIComponent(nomEntreprise)}`;
    
    return this.http.get<Personne[]>(url)
      .pipe(
        map(personnes => {
          return personnes.map(p => {
            const personneCorrigee: Personne = { ...p };
            
            if (p.age === undefined || p.age === null || isNaN(Number(p.age)) || Number(p.age) === 0) {
              personneCorrigee.age = this.calculerAge(p.dateNaissance);
            } else {
              personneCorrigee.age = Number(p.age);
            }
            
            if (!Array.isArray(p.emploisActuels)) {
              personneCorrigee.emploisActuels = [];
            }
            
            return personneCorrigee;
          });
        })
      );
  }

  obtenirPersonneParId(id: number): Observable<Personne> {
    return this.http.get<Personne>(`${this.apiUrl}/${id}`)
      .pipe(
        map(personne => {
          const personneCorrigee: Personne = { ...personne };
          
          if (personne.age === undefined || personne.age === null || isNaN(Number(personne.age)) || Number(personne.age) === 0) {
            personneCorrigee.age = this.calculerAge(personne.dateNaissance);
          } else {
            personneCorrigee.age = Number(personne.age);
          }
          
          if (!Array.isArray(personne.emploisActuels)) {
            personneCorrigee.emploisActuels = [];
          }
          
          return personneCorrigee;
        }),
        tap(personne => {
          const personnes = this.personnesSubject.value;
          const index = personnes.findIndex(p => p.id === id);
          if (index !== -1) {
            personnes[index] = personne;
            this.personnesSubject.next([...personnes]);
          }
        })
      );
  }

  creerPersonne(personne: Personne): Observable<any> {
    return this.http.post(this.apiUrl, personne)
      .pipe(tap(() => this.rafraichirPersonnes()));
  }

  supprimerPersonne(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`)
      .pipe(tap(() => this.rafraichirPersonnes()));
  }
  
  rafraichirPersonnes(): void {
    this.http.get<Personne[]>(this.apiUrl).subscribe({
      next: (personnes) => {
        const personnesTraitees = personnes.map(p => {
          const personneCorrigee: Personne = { ...p };
          
          if (p.age === undefined || p.age === null || isNaN(Number(p.age)) || Number(p.age) === 0) {
            personneCorrigee.age = this.calculerAge(p.dateNaissance);
          } else {
            personneCorrigee.age = Number(p.age);
          }
          
          if (!Array.isArray(p.emploisActuels)) {
            personneCorrigee.emploisActuels = [];
          }
          
          return personneCorrigee;
        });
        
        this.personnesSubject.next(personnesTraitees);
      },
      error: () => {}
    });
  }

  obtenirPersonnesAvecEmplois(): Observable<Personne[]> {
    const url = `${this.apiUrl}/avec-emplois`;
    
    return this.http.get<Personne[]>(url)
      .pipe(
        map(personnes => this.normaliserPersonnes(personnes))
      );
  }
  
  private normaliserPersonnes(personnes: Personne[]): Personne[] {
    return personnes.map(p => {
      const personneCorrigee: Personne = { ...p };
      
      if (p.age === undefined || p.age === null || isNaN(Number(p.age)) || Number(p.age) === 0) {
        personneCorrigee.age = this.calculerAge(p.dateNaissance);
      } else {
        personneCorrigee.age = Number(p.age);
      }
      
      if (!Array.isArray(p.emploisActuels)) {
        personneCorrigee.emploisActuels = [];
      }
      
      return personneCorrigee;
    });
  }
} 