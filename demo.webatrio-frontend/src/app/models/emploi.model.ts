export interface Emploi {
  id?: number;
  personneId: number;
  nomEntreprise: string;
  titrePoste: string;
  dateDebut: string;
  dateFin?: string;
} 