import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFichePresence, getFichePresenceIdentifier } from '../fiche-presence.model';

export type EntityResponseType = HttpResponse<IFichePresence>;
export type EntityArrayResponseType = HttpResponse<IFichePresence[]>;

@Injectable({ providedIn: 'root' })
export class FichePresenceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/fiche-presences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(fichePresence: IFichePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fichePresence);
    return this.http
      .post<IFichePresence>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(fichePresence: IFichePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fichePresence);
    return this.http
      .put<IFichePresence>(`${this.resourceUrl}/${getFichePresenceIdentifier(fichePresence) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(fichePresence: IFichePresence): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(fichePresence);
    return this.http
      .patch<IFichePresence>(`${this.resourceUrl}/${getFichePresenceIdentifier(fichePresence) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFichePresence>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFichePresence[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFichePresenceToCollectionIfMissing(
    fichePresenceCollection: IFichePresence[],
    ...fichePresencesToCheck: (IFichePresence | null | undefined)[]
  ): IFichePresence[] {
    const fichePresences: IFichePresence[] = fichePresencesToCheck.filter(isPresent);
    if (fichePresences.length > 0) {
      const fichePresenceCollectionIdentifiers = fichePresenceCollection.map(
        fichePresenceItem => getFichePresenceIdentifier(fichePresenceItem)!
      );
      const fichePresencesToAdd = fichePresences.filter(fichePresenceItem => {
        const fichePresenceIdentifier = getFichePresenceIdentifier(fichePresenceItem);
        if (fichePresenceIdentifier == null || fichePresenceCollectionIdentifiers.includes(fichePresenceIdentifier)) {
          return false;
        }
        fichePresenceCollectionIdentifiers.push(fichePresenceIdentifier);
        return true;
      });
      return [...fichePresencesToAdd, ...fichePresenceCollection];
    }
    return fichePresenceCollection;
  }

  protected convertDateFromClient(fichePresence: IFichePresence): IFichePresence {
    return Object.assign({}, fichePresence, {
      date: fichePresence.date?.isValid() ? fichePresence.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((fichePresence: IFichePresence) => {
        fichePresence.date = fichePresence.date ? dayjs(fichePresence.date) : undefined;
      });
    }
    return res;
  }
}
