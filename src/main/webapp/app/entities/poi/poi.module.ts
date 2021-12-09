import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PoiComponent } from './list/poi.component';
import { PoiDetailComponent } from './detail/poi-detail.component';
import { PoiUpdateComponent } from './update/poi-update.component';
import { PoiDeleteDialogComponent } from './delete/poi-delete-dialog.component';
import { PoiRoutingModule } from './route/poi-routing.module';

@NgModule({
  imports: [SharedModule, PoiRoutingModule],
  declarations: [PoiComponent, PoiDetailComponent, PoiUpdateComponent, PoiDeleteDialogComponent],
  entryComponents: [PoiDeleteDialogComponent],
})
export class PoiModule {}
