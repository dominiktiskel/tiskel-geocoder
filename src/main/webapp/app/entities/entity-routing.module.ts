import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'setting',
        data: { pageTitle: 'tiskelGeocoderApp.setting.home.title' },
        loadChildren: () => import('./setting/setting.module').then(m => m.SettingModule),
      },
      {
        path: 'source',
        data: { pageTitle: 'tiskelGeocoderApp.source.home.title' },
        loadChildren: () => import('./source/source.module').then(m => m.SourceModule),
      },
      {
        path: 'poi-group',
        data: { pageTitle: 'tiskelGeocoderApp.poiGroup.home.title' },
        loadChildren: () => import('./poi-group/poi-group.module').then(m => m.PoiGroupModule),
      },
      {
        path: 'poi',
        data: { pageTitle: 'tiskelGeocoderApp.poi.home.title' },
        loadChildren: () => import('./poi/poi.module').then(m => m.PoiModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
