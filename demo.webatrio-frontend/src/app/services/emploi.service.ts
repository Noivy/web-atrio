import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Emploi } from '../models/emploi.model';

@Injectable({
  providedIn: 'root'
})
export class EmploiService {
  private apiUrl = 'http://localhost:8080/api/emplois';

  constructor(private http: HttpClient) { }

  obtenirTousEmplois(): Observable<Emploi[]> {
    return this.http.get<Emploi[]>(this.apiUrl);
  }

  obtenirEmploisParPersonne(personneId: number): Observable<Emploi[]> {
    return this.http.get<Emploi[]>(`${this.apiUrl}/personne/${personneId}`);
  }

  obtenirEmploiParId(id: number): Observable<Emploi> {
    return this.http.get<Emploi>(`${this.apiUrl}/${id}`);
  }

  ajouterEmploi(emploi: Emploi, personneId: number): Observable<Emploi> {
    return this.http.post<Emploi>(`${this.apiUrl}/personne/${personneId}`, emploi);
  }

  supprimerEmploi(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
} 