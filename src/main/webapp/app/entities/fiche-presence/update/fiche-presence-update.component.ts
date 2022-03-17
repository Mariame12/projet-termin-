import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFichePresence, FichePresence } from '../fiche-presence.model';
import { FichePresenceService } from '../service/fiche-presence.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IConsultant } from 'app/entities/consultant/consultant.model';
import { ConsultantService } from 'app/entities/consultant/service/consultant.service';

@Component({
  selector: 'jhi-fiche-presence-update',
  templateUrl: './fiche-presence-update.component.html',
})
export class FichePresenceUpdateComponent implements OnInit {
  isSaving = false;

  consultantsSharedCollection: IConsultant[] = [];

  editForm = this.fb.group({
    id: [],
    activites: [],
    heuredebut: [],
    commentaire: [],
    heurefin: [],
    date: [],
    consultant: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fichePresenceService: FichePresenceService,
    protected consultantService: ConsultantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fichePresence }) => {
      this.updateForm(fichePresence);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('projetApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fichePresence = this.createFromForm();
    if (fichePresence.id !== undefined) {
      this.subscribeToSaveResponse(this.fichePresenceService.update(fichePresence));
    } else {
      this.subscribeToSaveResponse(this.fichePresenceService.create(fichePresence));
    }
  }

  trackConsultantById(index: number, item: IConsultant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFichePresence>>): void {
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

  protected updateForm(fichePresence: IFichePresence): void {
    this.editForm.patchValue({
      id: fichePresence.id,
      activites: fichePresence.activites,
      heuredebut: fichePresence.heuredebut,
      commentaire: fichePresence.commentaire,
      heurefin: fichePresence.heurefin,
      date: fichePresence.date,
      consultant: fichePresence.consultant,
    });

    this.consultantsSharedCollection = this.consultantService.addConsultantToCollectionIfMissing(
      this.consultantsSharedCollection,
      fichePresence.consultant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.consultantService
      .query()
      .pipe(map((res: HttpResponse<IConsultant[]>) => res.body ?? []))
      .pipe(
        map((consultants: IConsultant[]) =>
          this.consultantService.addConsultantToCollectionIfMissing(consultants, this.editForm.get('consultant')!.value)
        )
      )
      .subscribe((consultants: IConsultant[]) => (this.consultantsSharedCollection = consultants));
  }

  protected createFromForm(): IFichePresence {
    return {
      ...new FichePresence(),
      id: this.editForm.get(['id'])!.value,
      activites: this.editForm.get(['activites'])!.value,
      heuredebut: this.editForm.get(['heuredebut'])!.value,
      commentaire: this.editForm.get(['commentaire'])!.value,
      heurefin: this.editForm.get(['heurefin'])!.value,
      date: this.editForm.get(['date'])!.value,
      consultant: this.editForm.get(['consultant'])!.value,
    };
  }
}
