import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISource, getSourceIdentifier } from '../source.model';

export type EntityResponseType = HttpResponse<ISource>;
export type EntityArrayResponseType = HttpResponse<ISource[]>;

@Injectable({ providedIn: 'root' })
export class SourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sources');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(source: ISource): Observable<EntityResponseType> {
    return this.http.post<ISource>(this.resourceUrl, source, { observe: 'response' });
  }

  update(source: ISource): Observable<EntityResponseType> {
    return this.http.put<ISource>(`${this.resourceUrl}/${getSourceIdentifier(source) as number}`, source, { observe: 'response' });
  }

  partialUpdate(source: ISource): Observable<EntityResponseType> {
    return this.http.patch<ISource>(`${this.resourceUrl}/${getSourceIdentifier(source) as number}`, source, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSourceToCollectionIfMissing(sourceCollection: ISource[], ...sourcesToCheck: (ISource | null | undefined)[]): ISource[] {
    const sources: ISource[] = sourcesToCheck.filter(isPresent);
    if (sources.length > 0) {
      const sourceCollectionIdentifiers = sourceCollection.map(sourceItem => getSourceIdentifier(sourceItem)!);
      const sourcesToAdd = sources.filter(sourceItem => {
        const sourceIdentifier = getSourceIdentifier(sourceItem);
        if (sourceIdentifier == null || sourceCollectionIdentifiers.includes(sourceIdentifier)) {
          return false;
        }
        sourceCollectionIdentifiers.push(sourceIdentifier);
        return true;
      });
      return [...sourcesToAdd, ...sourceCollection];
    }
    return sourceCollection;
  }
}
