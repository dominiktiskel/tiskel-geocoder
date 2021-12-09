import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PoiGroupComponent } from '../list/poi-group.component';
import { PoiGroupDetailComponent } from '../detail/poi-group-detail.component';
import { PoiGroupUpdateComponent } from '../update/poi-group-update.component';
import { PoiGroupRoutingResolveService } from './poi-group-routing-resolve.service';

const poiGroupRoute: Routes = [
  {
    path: '',
    component: PoiGroupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PoiGroupDetailComponent,
    resolve: {
      poiGroup: PoiGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PoiGroupUpdateComponent,
    resolve: {
      poiGroup: PoiGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PoiGroupUpdateComponent,
    resolve: {
      poiGroup: PoiGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(poiGroupRoute)],
  exports: [RouterModule],
})
export class PoiGroupRoutingModule {}
