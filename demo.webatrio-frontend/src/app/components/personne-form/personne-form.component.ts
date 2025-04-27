import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PersonneService } from '../../services/personne.service';

@Component({
  selector: 'app-personne-form',
  templateUrl: './personne-form.component.html',
  styleUrls: ['./personne-form.component.css']
})
export class PersonneFormComponent implements OnInit {
  personneForm: FormGroup;
  messageErreur: string = '';
  messageSucces: string = '';
  soumissionEnCours: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private personneService: PersonneService
  ) {
    this.personneForm = this.formBuilder.group({
      prenom: ['', [Validators.required]],
      nom: ['', [Validators.required]],
      dateNaissance: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
  }

  onSubmit(): void {
    if (this.personneForm.invalid) {
      return;
    }

    this.soumissionEnCours = true;
    this.messageErreur = '';
    this.messageSucces = '';

    const personne = this.personneForm.value;
    
    this.personneService.creerPersonne(personne).subscribe({
      next: (reponse) => {
        this.soumissionEnCours = false;
        this.messageSucces = 'Personne enregistrée avec succès!';
        this.personneForm.reset();
      },
      error: (erreur) => {
        this.soumissionEnCours = false;
        if (erreur.error && typeof erreur.error === 'string' && erreur.error.includes('150 ans')) {
          this.messageErreur = "L'âge de la personne ne peut pas dépasser 150 ans";
        } else {
          this.messageErreur = "Une erreur s'est produite lors de l'enregistrement de la personne";
        }
      }
    });
  }

  validerDateNaissance(): boolean {
    const dateNaissance = this.personneForm.get('dateNaissance')?.value;
    if (!dateNaissance) return true;
    
    const dateActuelle = new Date();
    const dateNaissanceObj = new Date(dateNaissance);
    
    const differenceAnnees = dateActuelle.getFullYear() - dateNaissanceObj.getFullYear();
    
    return differenceAnnees < 150;
  }
} 