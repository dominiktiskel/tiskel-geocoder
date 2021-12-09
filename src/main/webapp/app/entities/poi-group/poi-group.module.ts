import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PoiGroupComponent } from './list/poi-group.component';
import { PoiGroupDetailComponent } from './detail/poi-group-detail.component';
import { PoiGroupUpdateComponent } from './update/poi-group-update.component';
import { PoiGroupDeleteDialogComponent } from './delete/poi-group-delete-dialog.component';
import { PoiGroupRoutingModule } from './route/poi-group-routing.module';

@NgModule({
  imports: [SharedModule, PoiGroupRoutingModule],
  declarations: [PoiGroupComponent, PoiGroupDetailComponent, PoiGroupUpdateComponent, PoiGroupDeleteDialogComponent],
  entryComponents: [PoiGroupDeleteDialogComponent],
})
export class PoiGroupModule {}
