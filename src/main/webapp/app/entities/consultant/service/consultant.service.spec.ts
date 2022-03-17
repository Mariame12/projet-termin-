import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConsultant, Consultant } from '../consultant.model';

import { ConsultantService } from './consultant.service';

describe('Service Tests', () => {
  describe('Consultant Service', () => {
    let service: ConsultantService;
    let httpMock: HttpTestingController;
    let elemDefault: IConsultant;
    let expectedResult: IConsultant | IConsultant[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConsultantService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        prenom: 'AAAAAAA',
        fonction: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Consultant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Consultant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Consultant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            fonction: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Consultant', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
            fonction: 'BBBBBB',
          },
          new Consultant()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Consultant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            fonction: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Consultant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConsultantToCollectionIfMissing', () => {
        it('should add a Consultant to an empty array', () => {
          const consultant: IConsultant = { id: 123 };
          expectedResult = service.addConsultantToCollectionIfMissing([], consultant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consultant);
        });

        it('should not add a Consultant to an array that contains it', () => {
          const consultant: IConsultant = { id: 123 };
          const consultantCollection: IConsultant[] = [
            {
              ...consultant,
            },
            { id: 456 },
          ];
          expectedResult = service.addConsultantToCollectionIfMissing(consultantCollection, consultant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Consultant to an array that doesn't contain it", () => {
          const consultant: IConsultant = { id: 123 };
          const consultantCollection: IConsultant[] = [{ id: 456 }];
          expectedResult = service.addConsultantToCollectionIfMissing(consultantCollection, consultant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consultant);
        });

        it('should add only unique Consultant to an array', () => {
          const consultantArray: IConsultant[] = [{ id: 123 }, { id: 456 }, { id: 25293 }];
          const consultantCollection: IConsultant[] = [{ id: 123 }];
          expectedResult = service.addConsultantToCollectionIfMissing(consultantCollection, ...consultantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const consultant: IConsultant = { id: 123 };
          const consultant2: IConsultant = { id: 456 };
          expectedResult = service.addConsultantToCollectionIfMissing([], consultant, consultant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consultant);
          expect(expectedResult).toContain(consultant2);
        });

        it('should accept null and undefined values', () => {
          const consultant: IConsultant = { id: 123 };
          expectedResult = service.addConsultantToCollectionIfMissing([], null, consultant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consultant);
        });

        it('should return initial array if no Consultant is added', () => {
          const consultantCollection: IConsultant[] = [{ id: 123 }];
          expectedResult = service.addConsultantToCollectionIfMissing(consultantCollection, undefined, null);
          expect(expectedResult).toEqual(consultantCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
