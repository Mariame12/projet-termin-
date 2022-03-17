import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFichePresence } from '../fiche-presence.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-fiche-presence-detail',
  templateUrl: './fiche-presence-detail.component.html',
})
export class FichePresenceDetailComponent implements OnInit {
  fichePresence: IFichePresence | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fichePresence }) => {
      this.fichePresence = fichePresence;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
