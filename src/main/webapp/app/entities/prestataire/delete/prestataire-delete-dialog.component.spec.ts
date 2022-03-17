jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PrestataireService } from '../service/prestataire.service';

import { PrestataireDeleteDialogComponent } from './prestataire-delete-dialog.component';

describe('Component Tests', () => {
  describe('Prestataire Management Delete Component', () => {
    let comp: PrestataireDeleteDialogComponent;
    let fixture: ComponentFixture<PrestataireDeleteDialogComponent>;
    let service: PrestataireService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PrestataireDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PrestataireDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PrestataireDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PrestataireService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
