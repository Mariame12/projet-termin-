import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'prestataire',
        data: { pageTitle: 'projetApp.prestataire.home.title' },
        loadChildren: () => import('./prestataire/prestataire.module').then(m => m.PrestataireModule),
      },
      {
        path: 'consultant',
        data: { pageTitle: 'projetApp.consultant.home.title' },
        loadChildren: () => import('./consultant/consultant.module').then(m => m.ConsultantModule),
      },
      {
        path: 'fiche-presence',
        data: { pageTitle: 'projetApp.fichePresence.home.title' },
        loadChildren: () => import('./fiche-presence/fiche-presence.module').then(m => m.FichePresenceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
