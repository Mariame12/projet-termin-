import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPrestataire, getPrestataireIdentifier } from '../prestataire.model';

export type EntityResponseType = HttpResponse<IPrestataire>;
export type EntityArrayResponseType = HttpResponse<IPrestataire[]>;

@Injectable({ providedIn: 'root' })
export class PrestataireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/prestataires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(prestataire: IPrestataire): Observable<EntityResponseType> {
    return this.http.post<IPrestataire>(this.resourceUrl, prestataire, { observe: 'response' });
  }

  update(prestataire: IPrestataire): Observable<EntityResponseType> {
    return this.http.put<IPrestataire>(`${this.resourceUrl}/${getPrestataireIdentifier(prestataire) as number}`, prestataire, {
      observe: 'response',
    });
  }

  partialUpdate(prestataire: IPrestataire): Observable<EntityResponseType> {
    return this.http.patch<IPrestataire>(`${this.resourceUrl}/${getPrestataireIdentifier(prestataire) as number}`, prestataire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPrestataire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPrestataire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPrestataireToCollectionIfMissing(
    prestataireCollection: IPrestataire[],
    ...prestatairesToCheck: (IPrestataire | null | undefined)[]
  ): IPrestataire[] {
    const prestataires: IPrestataire[] = prestatairesToCheck.filter(isPresent);
    if (prestataires.length > 0) {
      const prestataireCollectionIdentifiers = prestataireCollection.map(prestataireItem => getPrestataireIdentifier(prestataireItem)!);
      const prestatairesToAdd = prestataires.filter(prestataireItem => {
        const prestataireIdentifier = getPrestataireIdentifier(prestataireItem);
        if (prestataireIdentifier == null || prestataireCollectionIdentifiers.includes(prestataireIdentifier)) {
          return false;
        }
        prestataireCollectionIdentifiers.push(prestataireIdentifier);
        return true;
      });
      return [...prestatairesToAdd, ...prestataireCollection];
    }
    return prestataireCollection;
  }
}
