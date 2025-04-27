import { Emploi } from './emploi.model';

export interface Personne {
  id?: number;
  prenom: string;
  nom: string;
  dateNaissance: string;
  age: number;
  emploisActuels: Emploi[];
} 