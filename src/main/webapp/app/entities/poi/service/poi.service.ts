import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPoi, getPoiIdentifier } from '../poi.model';

export type EntityResponseType = HttpResponse<IPoi>;
export type EntityArrayResponseType = HttpResponse<IPoi[]>;

@Injectable({ providedIn: 'root' })
export class PoiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pois');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(poi: IPoi): Observable<EntityResponseType> {
    return this.http.post<IPoi>(this.resourceUrl, poi, { observe: 'response' });
  }

  update(poi: IPoi): Observable<EntityResponseType> {
    return this.http.put<IPoi>(`${this.resourceUrl}/${getPoiIdentifier(poi) as number}`, poi, { observe: 'response' });
  }

  partialUpdate(poi: IPoi): Observable<EntityResponseType> {
    return this.http.patch<IPoi>(`${this.resourceUrl}/${getPoiIdentifier(poi) as number}`, poi, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPoi>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPoi[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPoiToCollectionIfMissing(poiCollection: IPoi[], ...poisToCheck: (IPoi | null | undefined)[]): IPoi[] {
    const pois: IPoi[] = poisToCheck.filter(isPresent);
    if (pois.length > 0) {
      const poiCollectionIdentifiers = poiCollection.map(poiItem => getPoiIdentifier(poiItem)!);
      const poisToAdd = pois.filter(poiItem => {
        const poiIdentifier = getPoiIdentifier(poiItem);
        if (poiIdentifier == null || poiCollectionIdentifiers.includes(poiIdentifier)) {
          return false;
        }
        poiCollectionIdentifiers.push(poiIdentifier);
        return true;
      });
      return [...poisToAdd, ...poiCollection];
    }
    return poiCollection;
  }
}
