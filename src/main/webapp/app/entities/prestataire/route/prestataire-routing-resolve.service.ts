import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrestataire, Prestataire } from '../prestataire.model';
import { PrestataireService } from '../service/prestataire.service';

@Injectable({ providedIn: 'root' })
export class PrestataireRoutingResolveService implements Resolve<IPrestataire> {
  constructor(protected service: PrestataireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrestataire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prestataire: HttpResponse<Prestataire>) => {
          if (prestataire.body) {
            return of(prestataire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Prestataire());
  }
}
