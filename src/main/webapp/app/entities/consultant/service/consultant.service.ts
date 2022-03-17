import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IConsultant, getConsultantIdentifier } from '../consultant.model';

export type EntityResponseType = HttpResponse<IConsultant>;
export type EntityArrayResponseType = HttpResponse<IConsultant[]>;

@Injectable({ providedIn: 'root' })
export class ConsultantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/consultants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(consultant: IConsultant): Observable<EntityResponseType> {
    return this.http.post<IConsultant>(this.resourceUrl, consultant, { observe: 'response' });
  }

  update(consultant: IConsultant): Observable<EntityResponseType> {
    return this.http.put<IConsultant>(`${this.resourceUrl}/${getConsultantIdentifier(consultant) as number}`, consultant, {
      observe: 'response',
    });
  }

  partialUpdate(consultant: IConsultant): Observable<EntityResponseType> {
    return this.http.patch<IConsultant>(`${this.resourceUrl}/${getConsultantIdentifier(consultant) as number}`, consultant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IConsultant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IConsultant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addConsultantToCollectionIfMissing(
    consultantCollection: IConsultant[],
    ...consultantsToCheck: (IConsultant | null | undefined)[]
  ): IConsultant[] {
    const consultants: IConsultant[] = consultantsToCheck.filter(isPresent);
    if (consultants.length > 0) {
      const consultantCollectionIdentifiers = consultantCollection.map(consultantItem => getConsultantIdentifier(consultantItem)!);
      const consultantsToAdd = consultants.filter(consultantItem => {
        const consultantIdentifier = getConsultantIdentifier(consultantItem);
        if (consultantIdentifier == null || consultantCollectionIdentifiers.includes(consultantIdentifier)) {
          return false;
        }
        consultantCollectionIdentifiers.push(consultantIdentifier);
        return true;
      });
      return [...consultantsToAdd, ...consultantCollection];
    }
    return consultantCollection;
  }
}
