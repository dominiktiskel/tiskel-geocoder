import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PoiComponent } from '../list/poi.component';
import { PoiDetailComponent } from '../detail/poi-detail.component';
import { PoiUpdateComponent } from '../update/poi-update.component';
import { PoiRoutingResolveService } from './poi-routing-resolve.service';

const poiRoute: Routes = [
  {
    path: '',
    component: PoiComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PoiDetailComponent,
    resolve: {
      poi: PoiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PoiUpdateComponent,
    resolve: {
      poi: PoiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PoiUpdateComponent,
    resolve: {
      poi: PoiRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(poiRoute)],
  exports: [RouterModule],
})
export class PoiRoutingModule {}
