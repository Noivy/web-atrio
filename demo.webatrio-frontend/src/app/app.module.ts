import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { PersonneFormComponent } from './components/personne-form/personne-form.component';

const routes: Routes = [
  { path: '', redirectTo: '/nouvelle-personne', pathMatch: 'full' },
  { path: 'nouvelle-personne', component: PersonneFormComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    PersonneFormComponent
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