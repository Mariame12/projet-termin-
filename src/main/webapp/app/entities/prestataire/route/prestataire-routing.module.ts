import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PrestataireComponent } from '../list/prestataire.component';
import { PrestataireDetailComponent } from '../detail/prestataire-detail.component';
import { PrestataireUpdateComponent } from '../update/prestataire-update.component';
import { PrestataireRoutingResolveService } from './prestataire-routing-resolve.service';

const prestataireRoute: Routes = [
  {
    path: '',
    component: PrestataireComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PrestataireDetailComponent,
    resolve: {
      prestataire: PrestataireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PrestataireUpdateComponent,
    resolve: {
      prestataire: PrestataireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PrestataireUpdateComponent,
    resolve: {
      prestataire: PrestataireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(prestataireRoute)],
  exports: [RouterModule],
})
export class PrestataireRoutingModule {}
