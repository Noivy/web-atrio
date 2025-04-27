import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EmploiService } from '../../services/emploi.service';
import { PersonneService } from '../../services/personne.service';
import { Personne } from '../../models/personne.model';

@Component({
  selector: 'app-emploi-form',
  templateUrl: './emploi-form.component.html',
  styleUrls: ['./emploi-form.component.css']
})
export class EmploiFormComponent implements OnInit {
  emploiForm: FormGroup;
  personneId: number = 0;
  personne: Personne | null = null;
  messageErreur: string = '';
  messageSucces: string = '';
  soumissionEnCours: boolean = false;
  dateAujourdhui: string = new Date().toISOString().split('T')[0];

  constructor(
    private formBuilder: FormBuilder,
    private emploiService: EmploiService,
    private personneService: PersonneService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.emploiForm = this.formBuilder.group({
      nomEntreprise: ['', [Validators.required]],
      titrePoste: ['', [Validators.required]],
      dateDebut: ['', [Validators.required]],
      dateFin: ['']
    });
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.personneId = +params['personneId'];
      if (this.personneId) {
        this.chargerPersonne();
      }
    });
  }

  chargerPersonne(): void {
    this.personneService.obtenirPersonneParId(this.personneId).subscribe({
      next: (data) => {
        this.personne = data;
        console.log('Personne chargée:', this.personne);
        console.log('Emplois actuels:', this.personne.emploisActuels?.length || 0);
      },
      error: () => {
        this.messageErreur = "Personne introuvable";
      }
    });
  }

  onSubmit(): void {
    if (this.emploiForm.invalid) {
      return;
    }

    this.soumissionEnCours = true;
    this.messageErreur = '';
    this.messageSucces = '';

    const emploi = this.emploiForm.value;
    
    // Vérifier si la date de fin est vide (emploi actuel)
    if (emploi.dateFin === '') {
      emploi.dateFin = null;
      console.log('Emploi actuel: date de fin est null');
    }
    
    console.log('Envoi de l\'emploi au serveur:', emploi);
    
    this.emploiService.ajouterEmploi(emploi, this.personneId).subscribe({
      next: (resultat) => {
        console.log('Emploi ajouté avec succès:', resultat);
        this.soumissionEnCours = false;
        this.messageSucces = 'Emploi ajouté avec succès!';
        
        // Recharger les données de la personne pour mettre à jour les emplois actuels
        this.personneService.obtenirPersonneParId(this.personneId).subscribe(personneActualisee => {
          console.log('Personne actualisée après ajout d\'emploi:', personneActualisee);
          console.log('Emplois actuels après ajout:', personneActualisee.emploisActuels?.length || 0);
        });
        
        setTimeout(() => {
          this.router.navigate(['/personnes']);
        }, 1500);
      },
      error: (erreur) => {
        this.soumissionEnCours = false;
        console.error('Erreur reçue:', erreur);
        
        // Traiter le message d'erreur correctement
        if (typeof erreur.error === 'string') {
          this.messageErreur = erreur.error;
        } else if (erreur.error && erreur.error.message) {
          this.messageErreur = erreur.error.message;
        } else if (erreur.message) {
          this.messageErreur = erreur.message;
        } else {
          this.messageErreur = "Une erreur s'est produite lors de l'ajout de l'emploi";
        }
      }
    });
  }
} 