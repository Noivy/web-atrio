import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { PersonneFormComponent } from './components/personne-form/personne-form.component';
import { ListePersonnesComponent } from './components/liste-personnes/liste-personnes.component';
import { ListeEmploisComponent } from './components/liste-emplois/liste-emplois.component';
import { EmploiFormComponent } from './components/emploi-form/emploi-form.component';

const routes: Routes = [
  { path: '', redirectTo: '/personnes', pathMatch: 'full' },
  { path: 'personnes', component: ListePersonnesComponent },
  { path: 'nouvelle-personne', component: PersonneFormComponent },
  { path: 'emploi/nouveau/:personneId', component: EmploiFormComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    PersonneFormComponent,
    ListePersonnesComponent,
    ListeEmploisComponent,
    EmploiFormComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes)
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { } 