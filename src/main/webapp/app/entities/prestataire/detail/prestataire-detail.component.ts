import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrestataire } from '../prestataire.model';

@Component({
  selector: 'jhi-prestataire-detail',
  templateUrl: './prestataire-detail.component.html',
})
export class PrestataireDetailComponent implements OnInit {
  prestataire: IPrestataire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestataire }) => {
      this.prestataire = prestataire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
