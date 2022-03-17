import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPrestataire, Prestataire } from '../prestataire.model';
import { PrestataireService } from '../service/prestataire.service';

@Component({
  selector: 'jhi-prestataire-update',
  templateUrl: './prestataire-update.component.html',
})
export class PrestataireUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomPres: [],
    nomCont: [],
    prenomCont: [],
    email: [],
  });

  constructor(protected prestataireService: PrestataireService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prestataire }) => {
      this.updateForm(prestataire);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const prestataire = this.createFromForm();
    if (prestataire.id !== undefined) {
      this.subscribeToSaveResponse(this.prestataireService.update(prestataire));
    } else {
      this.subscribeToSaveResponse(this.prestataireService.create(prestataire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrestataire>>): void {
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

  protected updateForm(prestataire: IPrestataire): void {
    this.editForm.patchValue({
      id: prestataire.id,
      nomPres: prestataire.nomPres,
      nomCont: prestataire.nomCont,
      prenomCont: prestataire.prenomCont,
      email: prestataire.email,
    });
  }

  protected createFromForm(): IPrestataire {
    return {
      ...new Prestataire(),
      id: this.editForm.get(['id'])!.value,
      nomPres: this.editForm.get(['nomPres'])!.value,
      nomCont: this.editForm.get(['nomCont'])!.value,
      prenomCont: this.editForm.get(['prenomCont'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }
}
