import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Personne } from '../models/personne.model';

@Injectable({
  providedIn: 'root'
})
export class PersonneService {
  private apiUrl = 'http://localhost:8080/api/personnes';

  constructor(private http: HttpClient) { }

  obtenirToutesPersonnes(): Observable<Personne[]> {
    return this.http.get<Personne[]>(this.apiUrl);
  }

  obtenirPersonneParId(id: number): Observable<Personne> {
    return this.http.get<Personne>(`${this.apiUrl}/${id}`);
  }

  creerPersonne(personne: Personne): Observable<any> {
    return this.http.post(this.apiUrl, personne);
  }

  supprimerPersonne(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
} 