import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFichePresence } from '../fiche-presence.model';
import { FichePresenceService } from '../service/fiche-presence.service';

@Component({
  templateUrl: './fiche-presence-delete-dialog.component.html',
})
export class FichePresenceDeleteDialogComponent {
  fichePresence?: IFichePresence;

  constructor(protected fichePresenceService: FichePresenceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fichePresenceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
