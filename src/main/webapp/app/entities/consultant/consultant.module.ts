import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ConsultantComponent } from './list/consultant.component';
import { ConsultantDetailComponent } from './detail/consultant-detail.component';
import { ConsultantUpdateComponent } from './update/consultant-update.component';
import { ConsultantDeleteDialogComponent } from './delete/consultant-delete-dialog.component';
import { ConsultantRoutingModule } from './route/consultant-routing.module';

@NgModule({
  imports: [SharedModule, ConsultantRoutingModule],
  declarations: [ConsultantComponent, ConsultantDetailComponent, ConsultantUpdateComponent, ConsultantDeleteDialogComponent],
  entryComponents: [ConsultantDeleteDialogComponent],
})
export class ConsultantModule {}
