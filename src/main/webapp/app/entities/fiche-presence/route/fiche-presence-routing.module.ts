import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FichePresenceComponent } from '../list/fiche-presence.component';
import { FichePresenceDetailComponent } from '../detail/fiche-presence-detail.component';
import { FichePresenceUpdateComponent } from '../update/fiche-presence-update.component';
import { FichePresenceRoutingResolveService } from './fiche-presence-routing-resolve.service';

const fichePresenceRoute: Routes = [
  {
    path: '',
    component: FichePresenceComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FichePresenceDetailComponent,
    resolve: {
      fichePresence: FichePresenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FichePresenceUpdateComponent,
    resolve: {
      fichePresence: FichePresenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FichePresenceUpdateComponent,
    resolve: {
      fichePresence: FichePresenceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fichePresenceRoute)],
  exports: [RouterModule],
})
export class FichePresenceRoutingModule {}
