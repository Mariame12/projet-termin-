import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IConsultant, Consultant } from '../consultant.model';
import { ConsultantService } from '../service/consultant.service';

@Injectable({ providedIn: 'root' })
export class ConsultantRoutingResolveService implements Resolve<IConsultant> {
  constructor(protected service: ConsultantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IConsultant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((consultant: HttpResponse<Consultant>) => {
          if (consultant.body) {
            return of(consultant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Consultant());
  }
}
