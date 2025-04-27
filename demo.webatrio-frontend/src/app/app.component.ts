import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="container mt-4">
      <h1 class="mb-4">Gestion des Personnes et Emplois</h1>
      <nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
        <div class="container-fluid">
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link" routerLink="/personnes" routerLinkActive="active">Liste des Personnes</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/nouvelle-personne" routerLinkActive="active">Ajouter une Personne</a>
            </li>
          </ul>
        </div>
      </nav>
      <router-outlet></router-outlet>
    </div>
  `,
  styles: [`
    .navbar {
      border-radius: 5px;
      padding: 0.5rem 1rem;
    }
    .nav-link.active {
      font-weight: bold;
      color: #0d6efd !important;
    }
  `]
})
export class AppComponent {
  titre = 'demo-webatrio-frontend';
} 