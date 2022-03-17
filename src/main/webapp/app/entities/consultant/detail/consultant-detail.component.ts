import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConsultant } from '../consultant.model';

@Component({
  selector: 'jhi-consultant-detail',
  templateUrl: './consultant-detail.component.html',
})
export class ConsultantDetailComponent implements OnInit {
  consultant: IConsultant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultant }) => {
      this.consultant = consultant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
