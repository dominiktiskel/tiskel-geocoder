import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPoiGroup, PoiGroup } from '../poi-group.model';
import { PoiGroupService } from '../service/poi-group.service';

@Injectable({ providedIn: 'root' })
export class PoiGroupRoutingResolveService implements Resolve<IPoiGroup> {
  constructor(protected service: PoiGroupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPoiGroup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((poiGroup: HttpResponse<PoiGroup>) => {
          if (poiGroup.body) {
            return of(poiGroup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PoiGroup());
  }
}
