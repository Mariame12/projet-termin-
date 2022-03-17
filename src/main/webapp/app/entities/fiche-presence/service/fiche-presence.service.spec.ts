import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFichePresence, FichePresence } from '../fiche-presence.model';

import { FichePresenceService } from './fiche-presence.service';

describe('Service Tests', () => {
  describe('FichePresence Service', () => {
    let service: FichePresenceService;
    let httpMock: HttpTestingController;
    let elemDefault: IFichePresence;
    let expectedResult: IFichePresence | IFichePresence[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FichePresenceService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        activites: 'AAAAAAA',
        heuredebut: 'AAAAAAA',
        commentaire: 'AAAAAAA',
        heurefin: 'AAAAAAA',
        date: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FichePresence', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.create(new FichePresence()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FichePresence', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            activites: 'BBBBBB',
            heuredebut: 'BBBBBB',
            commentaire: 'BBBBBB',
            heurefin: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FichePresence', () => {
        const patchObject = Object.assign(
          {
            activites: 'BBBBBB',
            commentaire: 'BBBBBB',
          },
          new FichePresence()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FichePresence', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            activites: 'BBBBBB',
            heuredebut: 'BBBBBB',
            commentaire: 'BBBBBB',
            heurefin: 'BBBBBB',
            date: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            date: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FichePresence', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFichePresenceToCollectionIfMissing', () => {
        it('should add a FichePresence to an empty array', () => {
          const fichePresence: IFichePresence = { id: 123 };
          expectedResult = service.addFichePresenceToCollectionIfMissing([], fichePresence);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fichePresence);
        });

        it('should not add a FichePresence to an array that contains it', () => {
          const fichePresence: IFichePresence = { id: 123 };
          const fichePresenceCollection: IFichePresence[] = [
            {
              ...fichePresence,
            },
            { id: 456 },
          ];
          expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, fichePresence);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FichePresence to an array that doesn't contain it", () => {
          const fichePresence: IFichePresence = { id: 123 };
          const fichePresenceCollection: IFichePresence[] = [{ id: 456 }];
          expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, fichePresence);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fichePresence);
        });

        it('should add only unique FichePresence to an array', () => {
          const fichePresenceArray: IFichePresence[] = [{ id: 123 }, { id: 456 }, { id: 13319 }];
          const fichePresenceCollection: IFichePresence[] = [{ id: 123 }];
          expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, ...fichePresenceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fichePresence: IFichePresence = { id: 123 };
          const fichePresence2: IFichePresence = { id: 456 };
          expectedResult = service.addFichePresenceToCollectionIfMissing([], fichePresence, fichePresence2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fichePresence);
          expect(expectedResult).toContain(fichePresence2);
        });

        it('should accept null and undefined values', () => {
          const fichePresence: IFichePresence = { id: 123 };
          expectedResult = service.addFichePresenceToCollectionIfMissing([], null, fichePresence, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fichePresence);
        });

        it('should return initial array if no FichePresence is added', () => {
          const fichePresenceCollection: IFichePresence[] = [{ id: 123 }];
          expectedResult = service.addFichePresenceToCollectionIfMissing(fichePresenceCollection, undefined, null);
          expect(expectedResult).toEqual(fichePresenceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
