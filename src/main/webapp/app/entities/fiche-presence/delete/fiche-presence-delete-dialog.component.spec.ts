jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { FichePresenceService } from '../service/fiche-presence.service';

import { FichePresenceDeleteDialogComponent } from './fiche-presence-delete-dialog.component';

describe('Component Tests', () => {
  describe('FichePresence Management Delete Component', () => {
    let comp: FichePresenceDeleteDialogComponent;
    let fixture: ComponentFixture<FichePresenceDeleteDialogComponent>;
    let service: FichePresenceService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FichePresenceDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(FichePresenceDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FichePresenceDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FichePresenceService);
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
