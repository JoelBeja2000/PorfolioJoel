import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPresentacion, NewPresentacion } from '../presentacion.model';

export type PartialUpdatePresentacion = Partial<IPresentacion> & Pick<IPresentacion, 'id'>;

export type EntityResponseType = HttpResponse<IPresentacion>;
export type EntityArrayResponseType = HttpResponse<IPresentacion[]>;

@Injectable({ providedIn: 'root' })
export class PresentacionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/presentacions');

  create(presentacion: NewPresentacion): Observable<EntityResponseType> {
    return this.http.post<IPresentacion>(this.resourceUrl, presentacion, { observe: 'response' });
  }

  update(presentacion: IPresentacion): Observable<EntityResponseType> {
    return this.http.put<IPresentacion>(`${this.resourceUrl}/${this.getPresentacionIdentifier(presentacion)}`, presentacion, {
      observe: 'response',
    });
  }

  partialUpdate(presentacion: PartialUpdatePresentacion): Observable<EntityResponseType> {
    return this.http.patch<IPresentacion>(`${this.resourceUrl}/${this.getPresentacionIdentifier(presentacion)}`, presentacion, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPresentacion>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPresentacion[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPresentacionIdentifier(presentacion: Pick<IPresentacion, 'id'>): number {
    return presentacion.id;
  }

  comparePresentacion(o1: Pick<IPresentacion, 'id'> | null, o2: Pick<IPresentacion, 'id'> | null): boolean {
    return o1 && o2 ? this.getPresentacionIdentifier(o1) === this.getPresentacionIdentifier(o2) : o1 === o2;
  }

  addPresentacionToCollectionIfMissing<Type extends Pick<IPresentacion, 'id'>>(
    presentacionCollection: Type[],
    ...presentacionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const presentacions: Type[] = presentacionsToCheck.filter(isPresent);
    if (presentacions.length > 0) {
      const presentacionCollectionIdentifiers = presentacionCollection.map(presentacionItem =>
        this.getPresentacionIdentifier(presentacionItem),
      );
      const presentacionsToAdd = presentacions.filter(presentacionItem => {
        const presentacionIdentifier = this.getPresentacionIdentifier(presentacionItem);
        if (presentacionCollectionIdentifiers.includes(presentacionIdentifier)) {
          return false;
        }
        presentacionCollectionIdentifiers.push(presentacionIdentifier);
        return true;
      });
      return [...presentacionsToAdd, ...presentacionCollection];
    }
    return presentacionCollection;
  }
}
