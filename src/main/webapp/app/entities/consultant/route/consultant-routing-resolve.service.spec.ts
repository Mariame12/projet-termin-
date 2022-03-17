jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IConsultant, Consultant } from '../consultant.model';
import { ConsultantService } from '../service/consultant.service';

import { ConsultantRoutingResolveService } from './consultant-routing-resolve.service';

describe('Service Tests', () => {
  describe('Consultant routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ConsultantRoutingResolveService;
    let service: ConsultantService;
    let resultConsultant: IConsultant | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ConsultantRoutingResolveService);
      service = TestBed.inject(ConsultantService);
      resultConsultant = undefined;
    });

    describe('resolve', () => {
      it('should return IConsultant returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsultant = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConsultant).toEqual({ id: 123 });
      });

      it('should return new IConsultant if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsultant = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultConsultant).toEqual(new Consultant());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Consultant })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultConsultant = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultConsultant).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
