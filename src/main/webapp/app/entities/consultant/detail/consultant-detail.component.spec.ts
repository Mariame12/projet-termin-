import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConsultantDetailComponent } from './consultant-detail.component';

describe('Component Tests', () => {
  describe('Consultant Management Detail Component', () => {
    let comp: ConsultantDetailComponent;
    let fixture: ComponentFixture<ConsultantDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConsultantDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ consultant: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConsultantDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConsultantDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load consultant on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.consultant).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
