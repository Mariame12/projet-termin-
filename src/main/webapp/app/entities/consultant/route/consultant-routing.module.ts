import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ConsultantComponent } from '../list/consultant.component';
import { ConsultantDetailComponent } from '../detail/consultant-detail.component';
import { ConsultantUpdateComponent } from '../update/consultant-update.component';
import { ConsultantRoutingResolveService } from './consultant-routing-resolve.service';

const consultantRoute: Routes = [
  {
    path: '',
    component: ConsultantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ConsultantDetailComponent,
    resolve: {
      consultant: ConsultantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ConsultantUpdateComponent,
    resolve: {
      consultant: ConsultantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ConsultantUpdateComponent,
    resolve: {
      consultant: ConsultantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(consultantRoute)],
  exports: [RouterModule],
})
export class ConsultantRoutingModule {}
