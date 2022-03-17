jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FichePresenceService } from '../service/fiche-presence.service';
import { IFichePresence, FichePresence } from '../fiche-presence.model';
import { IConsultant } from 'app/entities/consultant/consultant.model';
import { ConsultantService } from 'app/entities/consultant/service/consultant.service';

import { FichePresenceUpdateComponent } from './fiche-presence-update.component';

describe('Component Tests', () => {
  describe('FichePresence Management Update Component', () => {
    let comp: FichePresenceUpdateComponent;
    let fixture: ComponentFixture<FichePresenceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fichePresenceService: FichePresenceService;
    let consultantService: ConsultantService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FichePresenceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FichePresenceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FichePresenceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fichePresenceService = TestBed.inject(FichePresenceService);
      consultantService = TestBed.inject(ConsultantService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Consultant query and add missing value', () => {
        const fichePresence: IFichePresence = { id: 456 };
        const consultant: IConsultant = { id: 97177 };
        fichePresence.consultant = consultant;

        const consultantCollection: IConsultant[] = [{ id: 18733 }];
        jest.spyOn(consultantService, 'query').mockReturnValue(of(new HttpResponse({ body: consultantCollection })));
        const additionalConsultants = [consultant];
        const expectedCollection: IConsultant[] = [...additionalConsultants, ...consultantCollection];
        jest.spyOn(consultantService, 'addConsultantToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ fichePresence });
        comp.ngOnInit();

        expect(consultantService.query).toHaveBeenCalled();
        expect(consultantService.addConsultantToCollectionIfMissing).toHaveBeenCalledWith(consultantCollection, ...additionalConsultants);
        expect(comp.consultantsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const fichePresence: IFichePresence = { id: 456 };
        const consultant: IConsultant = { id: 31449 };
        fichePresence.consultant = consultant;

        activatedRoute.data = of({ fichePresence });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fichePresence));
        expect(comp.consultantsSharedCollection).toContain(consultant);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FichePresence>>();
        const fichePresence = { id: 123 };
        jest.spyOn(fichePresenceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fichePresence });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fichePresence }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fichePresenceService.update).toHaveBeenCalledWith(fichePresence);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FichePresence>>();
        const fichePresence = new FichePresence();
        jest.spyOn(fichePresenceService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fichePresence });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fichePresence }));
        saveSubject.complete();

        // THEN
        expect(fichePresenceService.create).toHaveBeenCalledWith(fichePresence);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<FichePresence>>();
        const fichePresence = { id: 123 };
        jest.spyOn(fichePresenceService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ fichePresence });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fichePresenceService.update).toHaveBeenCalledWith(fichePresence);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackConsultantById', () => {
        it('Should return tracked Consultant primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackConsultantById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
