jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PrestataireService } from '../service/prestataire.service';
import { IPrestataire, Prestataire } from '../prestataire.model';

import { PrestataireUpdateComponent } from './prestataire-update.component';

describe('Component Tests', () => {
  describe('Prestataire Management Update Component', () => {
    let comp: PrestataireUpdateComponent;
    let fixture: ComponentFixture<PrestataireUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let prestataireService: PrestataireService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrestataireUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PrestataireUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PrestataireUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      prestataireService = TestBed.inject(PrestataireService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const prestataire: IPrestataire = { id: 456 };

        activatedRoute.data = of({ prestataire });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(prestataire));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Prestataire>>();
        const prestataire = { id: 123 };
        jest.spyOn(prestataireService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ prestataire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prestataire }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(prestataireService.update).toHaveBeenCalledWith(prestataire);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Prestataire>>();
        const prestataire = new Prestataire();
        jest.spyOn(prestataireService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ prestataire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: prestataire }));
        saveSubject.complete();

        // THEN
        expect(prestataireService.create).toHaveBeenCalledWith(prestataire);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Prestataire>>();
        const prestataire = { id: 123 };
        jest.spyOn(prestataireService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ prestataire });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(prestataireService.update).toHaveBeenCalledWith(prestataire);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
