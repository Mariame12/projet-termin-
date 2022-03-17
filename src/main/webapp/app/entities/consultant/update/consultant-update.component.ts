import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConsultant, Consultant } from '../consultant.model';
import { ConsultantService } from '../service/consultant.service';
import { IPrestataire } from 'app/entities/prestataire/prestataire.model';
import { PrestataireService } from 'app/entities/prestataire/service/prestataire.service';

@Component({
  selector: 'jhi-consultant-update',
  templateUrl: './consultant-update.component.html',
})
export class ConsultantUpdateComponent implements OnInit {
  isSaving = false;

  prestatairesSharedCollection: IPrestataire[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    fonction: [],
    prestataire: [],
  });

  constructor(
    protected consultantService: ConsultantService,
    protected prestataireService: PrestataireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consultant }) => {
      this.updateForm(consultant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consultant = this.createFromForm();
    if (consultant.id !== undefined) {
      this.subscribeToSaveResponse(this.consultantService.update(consultant));
    } else {
      this.subscribeToSaveResponse(this.consultantService.create(consultant));
    }
  }

  trackPrestataireById(index: number, item: IPrestataire): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsultant>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(consultant: IConsultant): void {
    this.editForm.patchValue({
      id: consultant.id,
      nom: consultant.nom,
      prenom: consultant.prenom,
      fonction: consultant.fonction,
      prestataire: consultant.prestataire,
    });

    this.prestatairesSharedCollection = this.prestataireService.addPrestataireToCollectionIfMissing(
      this.prestatairesSharedCollection,
      consultant.prestataire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.prestataireService
      .query()
      .pipe(map((res: HttpResponse<IPrestataire[]>) => res.body ?? []))
      .pipe(
        map((prestataires: IPrestataire[]) =>
          this.prestataireService.addPrestataireToCollectionIfMissing(prestataires, this.editForm.get('prestataire')!.value)
        )
      )
      .subscribe((prestataires: IPrestataire[]) => (this.prestatairesSharedCollection = prestataires));
  }

  protected createFromForm(): IConsultant {
    return {
      ...new Consultant(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      fonction: this.editForm.get(['fonction'])!.value,
      prestataire: this.editForm.get(['prestataire'])!.value,
    };
  }
}
