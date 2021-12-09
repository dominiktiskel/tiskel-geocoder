import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPoiGroup, getPoiGroupIdentifier } from '../poi-group.model';

export type EntityResponseType = HttpResponse<IPoiGroup>;
export type EntityArrayResponseType = HttpResponse<IPoiGroup[]>;

@Injectable({ providedIn: 'root' })
export class PoiGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/poi-groups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(poiGroup: IPoiGroup): Observable<EntityResponseType> {
    return this.http.post<IPoiGroup>(this.resourceUrl, poiGroup, { observe: 'response' });
  }

  update(poiGroup: IPoiGroup): Observable<EntityResponseType> {
    return this.http.put<IPoiGroup>(`${this.resourceUrl}/${getPoiGroupIdentifier(poiGroup) as number}`, poiGroup, { observe: 'response' });
  }

  partialUpdate(poiGroup: IPoiGroup): Observable<EntityResponseType> {
    return this.http.patch<IPoiGroup>(`${this.resourceUrl}/${getPoiGroupIdentifier(poiGroup) as number}`, poiGroup, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPoiGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPoiGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPoiGroupToCollectionIfMissing(poiGroupCollection: IPoiGroup[], ...poiGroupsToCheck: (IPoiGroup | null | undefined)[]): IPoiGroup[] {
    const poiGroups: IPoiGroup[] = poiGroupsToCheck.filter(isPresent);
    if (poiGroups.length > 0) {
      const poiGroupCollectionIdentifiers = poiGroupCollection.map(poiGroupItem => getPoiGroupIdentifier(poiGroupItem)!);
      const poiGroupsToAdd = poiGroups.filter(poiGroupItem => {
        const poiGroupIdentifier = getPoiGroupIdentifier(poiGroupItem);
        if (poiGroupIdentifier == null || poiGroupCollectionIdentifiers.includes(poiGroupIdentifier)) {
          return false;
        }
        poiGroupCollectionIdentifiers.push(poiGroupIdentifier);
        return true;
      });
      return [...poiGroupsToAdd, ...poiGroupCollection];
    }
    return poiGroupCollection;
  }
}
