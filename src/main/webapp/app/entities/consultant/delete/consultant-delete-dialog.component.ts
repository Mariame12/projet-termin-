import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IConsultant } from '../consultant.model';
import { ConsultantService } from '../service/consultant.service';

@Component({
  templateUrl: './consultant-delete-dialog.component.html',
})
export class ConsultantDeleteDialogComponent {
  consultant?: IConsultant;

  constructor(protected consultantService: ConsultantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.consultantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
