import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrestataireComponent } from './list/prestataire.component';
import { PrestataireDetailComponent } from './detail/prestataire-detail.component';
import { PrestataireUpdateComponent } from './update/prestataire-update.component';
import { PrestataireDeleteDialogComponent } from './delete/prestataire-delete-dialog.component';
import { PrestataireRoutingModule } from './route/prestataire-routing.module';

@NgModule({
  imports: [SharedModule, PrestataireRoutingModule],
  declarations: [PrestataireComponent, PrestataireDetailComponent, PrestataireUpdateComponent, PrestataireDeleteDialogComponent],
  entryComponents: [PrestataireDeleteDialogComponent],
})
export class PrestataireModule {}
