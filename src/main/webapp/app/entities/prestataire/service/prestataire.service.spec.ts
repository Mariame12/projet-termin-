import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrestataire, Prestataire } from '../prestataire.model';

import { PrestataireService } from './prestataire.service';

describe('Service Tests', () => {
  describe('Prestataire Service', () => {
    let service: PrestataireService;
    let httpMock: HttpTestingController;
    let elemDefault: IPrestataire;
    let expectedResult: IPrestataire | IPrestataire[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PrestataireService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nomPres: 'AAAAAAA',
        nomCont: 'AAAAAAA',
        prenomCont: 'AAAAAAA',
        email: 'AAAAAAA',
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

      it('should create a Prestataire', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Prestataire()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Prestataire', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPres: 'BBBBBB',
            nomCont: 'BBBBBB',
            prenomCont: 'BBBBBB',
            email: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Prestataire', () => {
        const patchObject = Object.assign(
          {
            nomPres: 'BBBBBB',
            prenomCont: 'BBBBBB',
          },
          new Prestataire()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Prestataire', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nomPres: 'BBBBBB',
            nomCont: 'BBBBBB',
            prenomCont: 'BBBBBB',
            email: 'BBBBBB',
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

      it('should delete a Prestataire', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPrestataireToCollectionIfMissing', () => {
        it('should add a Prestataire to an empty array', () => {
          const prestataire: IPrestataire = { id: 123 };
          expectedResult = service.addPrestataireToCollectionIfMissing([], prestataire);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prestataire);
        });

        it('should not add a Prestataire to an array that contains it', () => {
          const prestataire: IPrestataire = { id: 123 };
          const prestataireCollection: IPrestataire[] = [
            {
              ...prestataire,
            },
            { id: 456 },
          ];
          expectedResult = service.addPrestataireToCollectionIfMissing(prestataireCollection, prestataire);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Prestataire to an array that doesn't contain it", () => {
          const prestataire: IPrestataire = { id: 123 };
          const prestataireCollection: IPrestataire[] = [{ id: 456 }];
          expectedResult = service.addPrestataireToCollectionIfMissing(prestataireCollection, prestataire);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prestataire);
        });

        it('should add only unique Prestataire to an array', () => {
          const prestataireArray: IPrestataire[] = [{ id: 123 }, { id: 456 }, { id: 99495 }];
          const prestataireCollection: IPrestataire[] = [{ id: 123 }];
          expectedResult = service.addPrestataireToCollectionIfMissing(prestataireCollection, ...prestataireArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const prestataire: IPrestataire = { id: 123 };
          const prestataire2: IPrestataire = { id: 456 };
          expectedResult = service.addPrestataireToCollectionIfMissing([], prestataire, prestataire2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prestataire);
          expect(expectedResult).toContain(prestataire2);
        });

        it('should accept null and undefined values', () => {
          const prestataire: IPrestataire = { id: 123 };
          expectedResult = service.addPrestataireToCollectionIfMissing([], null, prestataire, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prestataire);
        });

        it('should return initial array if no Prestataire is added', () => {
          const prestataireCollection: IPrestataire[] = [{ id: 123 }];
          expectedResult = service.addPrestataireToCollectionIfMissing(prestataireCollection, undefined, null);
          expect(expectedResult).toEqual(prestataireCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
