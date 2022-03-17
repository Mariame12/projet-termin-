jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPrestataire, Prestataire } from '../prestataire.model';
import { PrestataireService } from '../service/prestataire.service';

import { PrestataireRoutingResolveService } from './prestataire-routing-resolve.service';

describe('Service Tests', () => {
  describe('Prestataire routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PrestataireRoutingResolveService;
    let service: PrestataireService;
    let resultPrestataire: IPrestataire | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PrestataireRoutingResolveService);
      service = TestBed.inject(PrestataireService);
      resultPrestataire = undefined;
    });

    describe('resolve', () => {
      it('should return IPrestataire returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrestataire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrestataire).toEqual({ id: 123 });
      });

      it('should return new IPrestataire if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrestataire = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPrestataire).toEqual(new Prestataire());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Prestataire })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPrestataire = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPrestataire).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
