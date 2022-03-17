jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConsultantService } from '../service/consultant.service';
import { IConsultant, Consultant } from '../consultant.model';
import { IPrestataire } from 'app/entities/prestataire/prestataire.model';
import { PrestataireService } from 'app/entities/prestataire/service/prestataire.service';

import { ConsultantUpdateComponent } from './consultant-update.component';

describe('Component Tests', () => {
  describe('Consultant Management Update Component', () => {
    let comp: ConsultantUpdateComponent;
    let fixture: ComponentFixture<ConsultantUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let consultantService: ConsultantService;
    let prestataireService: PrestataireService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConsultantUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConsultantUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConsultantUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      consultantService = TestBed.inject(ConsultantService);
      prestataireService = TestBed.inject(PrestataireService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Prestataire query and add missing value', () => {
        const consultant: IConsultant = { id: 456 };
        const prestataire: IPrestataire = { id: 91935 };
        consultant.prestataire = prestataire;

        const prestataireCollection: IPrestataire[] = [{ id: 37153 }];
        jest.spyOn(prestataireService, 'query').mockReturnValue(of(new HttpResponse({ body: prestataireCollection })));
        const additionalPrestataires = [prestataire];
        const expectedCollection: IPrestataire[] = [...additionalPrestataires, ...prestataireCollection];
        jest.spyOn(prestataireService, 'addPrestataireToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ consultant });
        comp.ngOnInit();

        expect(prestataireService.query).toHaveBeenCalled();
        expect(prestataireService.addPrestataireToCollectionIfMissing).toHaveBeenCalledWith(
          prestataireCollection,
          ...additionalPrestataires
        );
        expect(comp.prestatairesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const consultant: IConsultant = { id: 456 };
        const prestataire: IPrestataire = { id: 27654 };
        consultant.prestataire = prestataire;

        activatedRoute.data = of({ consultant });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(consultant));
        expect(comp.prestatairesSharedCollection).toContain(prestataire);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Consultant>>();
        const consultant = { id: 123 };
        jest.spyOn(consultantService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ consultant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consultant }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(consultantService.update).toHaveBeenCalledWith(consultant);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Consultant>>();
        const consultant = new Consultant();
        jest.spyOn(consultantService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ consultant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consultant }));
        saveSubject.complete();

        // THEN
        expect(consultantService.create).toHaveBeenCalledWith(consultant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Consultant>>();
        const consultant = { id: 123 };
        jest.spyOn(consultantService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ consultant });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(consultantService.update).toHaveBeenCalledWith(consultant);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPrestataireById', () => {
        it('Should return tracked Prestataire primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPrestataireById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
