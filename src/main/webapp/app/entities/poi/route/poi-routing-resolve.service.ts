import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPoi, Poi } from '../poi.model';
import { PoiService } from '../service/poi.service';

@Injectable({ providedIn: 'root' })
export class PoiRoutingResolveService implements Resolve<IPoi> {
  constructor(protected service: PoiService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPoi> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((poi: HttpResponse<Poi>) => {
          if (poi.body) {
            return of(poi.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Poi());
  }
}
