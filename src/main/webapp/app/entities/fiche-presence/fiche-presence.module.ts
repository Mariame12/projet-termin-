import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FichePresenceComponent } from './list/fiche-presence.component';
import { FichePresenceDetailComponent } from './detail/fiche-presence-detail.component';
import { FichePresenceUpdateComponent } from './update/fiche-presence-update.component';
import { FichePresenceDeleteDialogComponent } from './delete/fiche-presence-delete-dialog.component';
import { FichePresenceRoutingModule } from './route/fiche-presence-routing.module';

@NgModule({
  imports: [SharedModule, FichePresenceRoutingModule],
  declarations: [FichePresenceComponent, FichePresenceDetailComponent, FichePresenceUpdateComponent, FichePresenceDeleteDialogComponent],
  entryComponents: [FichePresenceDeleteDialogComponent],
})
export class FichePresenceModule {}
