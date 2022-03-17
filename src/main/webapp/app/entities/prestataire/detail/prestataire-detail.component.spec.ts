import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrestataireDetailComponent } from './prestataire-detail.component';

describe('Component Tests', () => {
  describe('Prestataire Management Detail Component', () => {
    let comp: PrestataireDetailComponent;
    let fixture: ComponentFixture<PrestataireDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PrestataireDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ prestataire: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PrestataireDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PrestataireDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load prestataire on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.prestataire).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
